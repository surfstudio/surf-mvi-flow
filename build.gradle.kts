import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {
    val kotlinVersion: String by project

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.6.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

plugins {
    id("com.diffplug.spotless")
    id("com.jfrog.artifactory")
    `maven-publish`
    id("com.github.ben-manes.versions")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlin.plugin.compose")
}

/**
 * Run ./gradlew dependencyUpdates to check for new updates
 * in dependencies used.
 * More info at: https://github.com/ben-manes/gradle-versions-plugin
 */
tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        // disallow release candidates as upgradable versions from stable versions
        isNonStable(candidate.version) && !isNonStable(currentVersion) ||
                // https://github.com/ben-manes/gradle-versions-plugin/issues/534#issuecomment-897236772
                candidate.group == "org.jacoco"
    }
}

subprojects {
    apply(plugin = "com.diffplug.spotless")

    spotless {
        kotlin {
            target("**/*.kt")
            licenseHeaderFile(file("${project.rootDir}/spotless/LicenseHeader"))
        }
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}