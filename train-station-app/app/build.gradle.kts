import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.*
import com.google.protobuf.gradle.*
import org.jetbrains.kotlin.config.KotlinCompilerVersion
import java.util.Properties
import java.io.FileInputStream

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.spotless)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.ksp)
    id("com.google.android.gms.oss-licenses-plugin")
    id("com.google.gms.google-services")
}

spotless {
    java {
        target("**/*.java")
        targetExclude("src/main/java/com/example/trainstationapp/data/grpc/**/*.java")
        googleJavaFormat().aosp()
        removeUnusedImports()
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
    kotlin {
        target("**/*.kt")
        targetExclude("src/main/java/com/example/trainstationapp/data/grpc/**/*.kt")
        ktfmt("0.44").kotlinlangStyle()
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
    format("misc") {
        target("**/*.gradle", "**/*.md", "**/.gitignore")
        indentWithSpaces()
        trimTrailingWhitespace()
        endWithNewline()
    }
    format("xml") {
        target("**/*.xml")
        indentWithSpaces()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

android {
    namespace = "com.example.trainstationapp"
    compileSdk = 33

    buildFeatures {
        compose = true
    }

    defaultConfig {
        applicationId = "com.example.trainstationapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        val secureProps = Properties()
        if (file("../secure.properties").exists()) {
            secureProps.load(FileInputStream(file("../secure.properties")))
        }
        resValue("string", "maps_api_key", (secureProps.getProperty("MAPS_API_KEY") ?: ""))
        resValue("string", "github_client_id", (secureProps.getProperty("GITHUB_CLIENT_ID") ?: ""))
        resValue("string", "github_client_secret", (secureProps.getProperty("GITHUB_CLIENT_SECRET") ?: ""))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }

        manifestPlaceholders["appAuthRedirectScheme"] = "com.example.trainstationapp"
    }

    signingConfigs {
        create("release") {
            val properties = Properties().apply {
                val propertiesFile = rootProject.file("key.properties")
                if (propertiesFile.exists()) {
                    load(FileInputStream(propertiesFile))
                }
            }
            keyAlias = properties.getProperty("keyAlias")
            keyPassword = properties.getProperty("keyPassword")
            storeFile = if (file(properties.getProperty("storeFile")).exists()) File(properties.getProperty("storeFile")) else null
            storePassword = properties.getProperty("storePassword")
        }
    }

    buildTypes {
        create("staging") {
            initWith(getByName("release"))
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            applicationIdSuffix = ".staging"
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.versions.protobuf}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${libs.versions.grpc}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${libs.versions.grpc.kotlin.plugin}:jdk8@jar"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc") {
                    option("lite")
                }
                id("grpckt") {
                    option("lite")
                }
            }
            it.builtins {
                id("java") {
                    option("lite")
                }
                id("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    // Android KTX
    implementation(libs.core.ktx)

    // Dagger Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Authentication
    implementation(libs.appauth)

    // gRPC
    implementation(libs.grpc.kotlin.stub)
    implementation(libs.grpc.protobuf.lite)
    implementation(libs.protobuf.kotlin.lite)
    runtimeOnly(libs.grpc.okhttp)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    ksp(libs.room.compiler)

    // Datastore
    implementation(libs.datastore)

    // Lifecycle
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.viewmodel.savedstate)

    // Navigation
    implementation(libs.navigation.compose)

    // UI
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    debugImplementation(libs.ui.tooling)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    debugImplementation(libs.ui.test.manifest)
    androidTestImplementation(libs.ui.test.junit4)

    // Google Services
    implementation(libs.maps.ktx)
    implementation(libs.play.services.maps)
    implementation(libs.android.maps.utils)
    implementation(libs.play.services.oss.licenses)

    // Logging
    implementation(libs.timber)

    // Paging
    implementation(libs.paging.runtime.ktx)
    implementation(libs.paging.compose)

    // JSON Serialization
    implementation(libs.kotlinx.serialization.json)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit2.kotlinx.serialization.converter)

    debugImplementation(libs.leakcanary)

    testImplementation(libs.mockk)

    testImplementation(libs.kotest.runner.junit4)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.property)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
