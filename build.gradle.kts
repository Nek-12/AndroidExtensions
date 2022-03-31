plugins {
    `kotlin-dsl`
}

rootProject.group = "com.nek12.androidutils"
rootProject.version = "0.7.0"

buildscript {

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.gradle}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
    }
}

allprojects {
    repositories {
        google()
        maven { url = uri("https://jitpack.io") }
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    when (name) {
        "app" -> apply(plugin = "com.android.application")
        "core-ktx" -> apply(plugin = "java-library")
        else -> apply(plugin = "android-library")
    }

}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}
