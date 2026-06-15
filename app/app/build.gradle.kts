import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.spotless)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.room)
}

spotless {
    java {
        target("**/*.java")
        importOrder()
        targetExclude("src/main/java/com/example/trainstationapp/data/grpc/**/*.java")
        googleJavaFormat().aosp()
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlin {
        target("**/*.kt", "**/*.kts")
        targetExclude("src/main/java/com/example/trainstationapp/data/grpc/**/*.kt")
        ktlint()
            .editorConfigOverride(
                mapOf(
                    "ktlint_standard_no-unused-imports" to "enabled",
                    "ktlint_standard_function-naming" to "disabled",
                ),
            ).customRuleSets(
                listOf(
                    "io.nlopez.compose.rules:ktlint:0.6.1",
                ),
            )
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        trimTrailingWhitespace()
        endWithNewline()
        ktlint()
    }
}

android {
    namespace = "com.example.trainstationapp"
    compileSdk = 37

    buildFeatures {
        compose = true
        buildConfig = true
        resValues = true
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    defaultConfig {
        applicationId = "com.example.trainstationapp"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
        val secureProps = Properties()
        if (file("../secure.properties").exists()) {
            secureProps.load(FileInputStream(file("../secure.properties")))
        }
        resValue("string", "maps_api_key", (secureProps.getProperty("MAPS_API_KEY") ?: ""))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }

        buildConfigField(
            "String",
            "OIDC_DISCOVERY_URL",
            "\"https://train.mnguyen.fr/dex/.well-known/openid-configuration\"",
        )

        buildConfigField(
            "String",
            "TRAIN_STATION_API_URL",
            "\"https://api.train.mnguyen.fr\"",
        )

        buildConfigField(
            "String",
            "CLIENT_ID",
            "\"train-station-app\"",
        )
    }

    signingConfigs {
        create("release") {
            val propertiesFile = rootProject.file("key.properties")
            if (propertiesFile.exists()) {
                val properties = Properties().apply { load(FileInputStream(propertiesFile)) }
                keyAlias = properties.getProperty("keyAlias")
                keyPassword = properties.getProperty("keyPassword")
                storeFile =
                    if (file(properties.getProperty("storeFile")).exists()) {
                        file(properties.getProperty("storeFile"))
                    } else {
                        null
                    }
                storePassword = properties.getProperty("storePassword")
            }
        }
    }

    buildTypes {
        create("staging") {
            initWith(getByName("release"))
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            applicationIdSuffix = ".staging"
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )

            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
    testOptions { unitTests.all { it.useJUnitPlatform() } }
}

protobuf {
    protoc { artifact = libs.protoc.get().toString() }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
                create("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.nimbus.jose.jwt)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.browser)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    // Android KTX
    implementation(libs.core.ktx)

    // Dagger Hilt
    implementation(libs.hilt.android)
    implementation(libs.hiltx.navigation.compose)
    ksp(libs.hilt.android.compiler)

    // ConnectRPC
    implementation(libs.connectrpc)
    implementation(libs.connectrpc.okhttp)
    implementation(libs.connect.kotlin.google.javalite.ext)
    implementation(libs.protobuf.kotlin.lite)
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
    implementation(libs.lifecycle.runtime.compose)

    // Navigation
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    // UI
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    debugImplementation(libs.ui.tooling)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.material.icons)
    implementation(libs.foundation)
    implementation(libs.constraintlayout)
    implementation(libs.constraintlayout.compose)
    implementation(libs.coordinatorlayout)
    debugImplementation(libs.ui.test.manifest)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)

    // Logging
    implementation(libs.timber)

    // MapLibre
    implementation(
        libs.maplibre.compose
            .get()
            .toString(),
    ) {
        exclude(group = "org.maplibre.gl", module = "android-sdk")
    }
    implementation(libs.maplibre)

    // Paging
    implementation(libs.paging.runtime.ktx)
    implementation(libs.paging.compose)

    // JSON Serialization
    implementation(libs.kotlinx.serialization.json)

    // Retrofit
    implementation(libs.retrofit)

    debugImplementation(libs.leakcanary)

    testImplementation(libs.mockk)

    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.property)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
