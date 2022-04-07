pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        // https://www.jfrog.com/confluence/display/JFROG/Gradle+Artifactory+Plugin
        id("com.jfrog.artifactory") version "4.28.0"
        // https://github.com/diffplug/spotless
        id("com.diffplug.spotless") version "6.3.0"
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