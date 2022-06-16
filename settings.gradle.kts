pluginManagement {
    val artifactoryVersion: String by settings

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        // https://www.jfrog.com/confluence/display/JFROG/Gradle+Artifactory+Plugin
        id("com.jfrog.artifactory") version artifactoryVersion
        // https://github.com/diffplug/spotless
        id("com.diffplug.spotless") version "6.7.2"
        // https://github.com/ben-manes/gradle-versions-plugin
        id("com.github.ben-manes.versions") version "0.42.0"
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