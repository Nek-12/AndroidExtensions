import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude
import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include

plugins {
    kotlin("jvm")
    `kotlin-dsl`
    alias(libs.plugins.detekt)
}

rootProject.group = "com.nek12.androidutils"
rootProject.version = "0.7.6"

buildscript {

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath(libs.android.gradle)
        classpath(libs.kotlin.gradle)
        classpath(libs.version.gradle)
    }
}

allprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "com.github.ben-manes.versions")

    detekt {
        source = objects.fileCollection().from(
            io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_SRC_DIR_JAVA,
            io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_TEST_SRC_DIR_JAVA,
            io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_SRC_DIR_KOTLIN,
            io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_TEST_SRC_DIR_KOTLIN,
        )
        buildUponDefaultConfig = true
        // baseline = file("$rootDir/config/detekt/baseline.xml")
    }

    dependencies {
        detektPlugins(rootProject.libs.detekt.formatting)
        detektPlugins(rootProject.libs.detekt.compose)
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = "1.8"
        reports {
            xml.required.set(false)
            html.required.set(true)
            txt.required.set(true)
            sarif.required.set(false)
        }
    }
    tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
        jvmTarget = "1.8"
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


tasks.register<io.gitlab.arturbosch.detekt.Detekt>("detektFormat") {
    description = "Formats whole project."
    parallel = true
    buildUponDefaultConfig = true
    autoCorrect = true
    setSource(file(projectDir))
    config.setFrom(File(rootDir, "detekt.yml"))
    include("**/*.kt", "**/*.kts")
    exclude("**/resources/**", "**/build/**", "**/.idea/**")
    reports {
        xml.required.set(false)
        html.required.set(false)
        txt.required.set(false)
    }
}

tasks.register<io.gitlab.arturbosch.detekt.Detekt>("detektAll") {
    description = "Runs the whole project at once."
    parallel = true
    buildUponDefaultConfig = true
    setSource(file(projectDir))
    config.setFrom(File(rootDir,"detekt.yml"))
    include("**/*.kt", "**/*.kts")
    exclude("**/resources/**", "**/build/**", "**/.idea/**")
    reports {
        xml.required.set(false)
        html.required.set(false)
        txt.required.set(false)
    }
}
