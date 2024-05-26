@file:Suppress("MissingPackageDeclaration")

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// must be top-level

object Config {

    const val group = "com.nek12"
    const val artifact = "androidutils"
    const val artifactId = "$group.$artifact"

    const val minSdk = 23
    const val compileSdk = 34
    val jvmTarget = JvmTarget.JVM_11
    val javaVersion = JavaVersion.VERSION_11
    const val version = "2.0.0"

    val kotlinCompilerArgs = listOf(
        "-opt-in=kotlin.RequiresOptIn",
        "-Xjvm-default=all",
        "-opt-in=kotlin.Experimental",
        "-opt-in=kotlin.RequiresOptIn",
        "-Xcontext-receivers",
    )

    val stabilityLevels = listOf("preview", "eap", "alpha", "beta", "m", "cr", "rc")
    object Detekt {

        const val configFile = "detekt.yml"
        val includedFiles = listOf("**/*.kt", "**/*.kts")
        val excludedFiles = listOf("**/resources/**", "**/build/**", "**/.idea/**")
    }
}
