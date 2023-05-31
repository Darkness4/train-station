// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath("com.google.android.gms:oss-licenses-plugin:0.10.6")
        classpath("com.google.gms:google-services:4.3.15")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlinKapt) apply false
    alias(libs.plugins.kotlinParcelize) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.hiltAndroid) apply false
    alias(libs.plugins.protobuf) apply false
    alias(libs.plugins.ksp) apply false
}
