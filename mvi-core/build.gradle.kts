plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
    id("com.jfrog.artifactory")
}

val composeVersion: String by project

val libraryConfig: () -> Unit by project.extra
val androidConfig: Any.() -> Unit by project.extra

libraryConfig()

android {
    androidConfig()

    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    api("androidx.compose.runtime:runtime:$composeVersion")
    testApi("junit:junit:4.13.2")
    testApi( "org.mockito.kotlin:mockito-kotlin:4.0.0")
    testApi("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
}