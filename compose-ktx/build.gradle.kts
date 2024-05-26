plugins {
    alias(libs.plugins.compose.compiler)
}
android {
    buildFeatures {
        compose = true
    }

    namespace = "${rootProject.group}.compose"
}

dependencies {
    implementation(project(":android-ktx"))
    implementation(libs.compose.ui)
    implementation(libs.compose.activity)
    implementation(libs.compose.foundation)
    implementation(libs.compose.graphics)
    implementation(libs.lifecycle.runtime)
    implementation(libs.androidx.core)
}
