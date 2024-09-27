plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlinx-serialization")
    id("org.jetbrains.kotlin.plugin.compose")
}

val composeVersion: String by project

android {
    namespace = "ru.surfstudio.mvi.flow.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "ru.surfstudio.mvi.flow.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
    }
}

dependencies {
    implementation(project(":mvi-mappers"))

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material)

    // android
    implementation(libs.androidx.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.fragment.ktx)

    // network
    implementation(libs.network.retrofit)
    implementation(libs.network.logging.interceptor)
    implementation(libs.network.retrofit2.kotlinx.serialization.converter)
    implementation(libs.network.kotlinx.serialization.json)

    // other
    implementation(libs.logging.timber)

    // test
    testImplementation(libs.test.junit)
    testImplementation(libs.test.junit.jupiter.api)
    testImplementation(libs.test.junit.android)
    testImplementation(libs.test.espresso.core)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.turbine)
    testImplementation(libs.test.kotlinx.coroutines.test)
}