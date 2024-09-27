pluginManagement {
    val artifactoryVersion: String by settings
    val kotlinVersion: String by settings

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        // https://www.jfrog.com/confluence/display/JFROG/Gradle+Artifactory+Plugin
        id("com.jfrog.artifactory") version artifactoryVersion
        // https://github.com/diffplug/spotless
        id("com.diffplug.spotless") version "6.25.0"
        // https://github.com/ben-manes/gradle-versions-plugin
        id("com.github.ben-manes.versions") version "0.51.0"
        // https://github.com/Kotlin/kotlinx.serialization
        kotlin("plugin.serialization") version kotlinVersion
        // https://developer.android.com/develop/ui/compose/compiler
        id("org.jetbrains.kotlin.plugin.compose") version kotlinVersion
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://artifactory.surfstudio.ru/artifactory/libs-release-local")
    }
}

rootProject.name = "surf-mvi-flow"
include(":mvi-core")
include(":mvi-mappers")
include(":sample")