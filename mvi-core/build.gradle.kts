plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
    id("com.jfrog.artifactory")
}

val libraryConfig: () -> Unit by project.extra
val androidConfig: Any.() -> Unit by project.extra

libraryConfig()

android {
    androidConfig()
}