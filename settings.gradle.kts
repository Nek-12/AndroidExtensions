pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

buildscript {
    repositories {
        google()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":room")
include(":android-ktx")
include(":material-ktx")
include(":preferences-ktx")
include(":safenavcontroller")
include(":viewbinding")
include(":compose-ktx")
include(":view-ktx")

rootProject.name = "AndroidUtils"
