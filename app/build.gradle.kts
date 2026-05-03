// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinParcelize) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.hiltAndroid) apply false
    alias(libs.plugins.protobuf) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.room) apply false
}
