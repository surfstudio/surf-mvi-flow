plugins {
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
}