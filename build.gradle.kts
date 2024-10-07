import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {

    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    `maven-publish`
    // https://www.jfrog.com/confluence/display/JFROG/Gradle+Artifactory+Plugin
    alias(libs.plugins.artifactory) apply false
    // https://github.com/diffplug/spotless
    alias(libs.plugins.spotless)
    // https://github.com/ben-manes/gradle-versions-plugin
    alias(libs.plugins.ben.manes.versions)
    // https://github.com/Kotlin/kotlinx.serialization
    alias(libs.plugins.kotlin.serialization) apply false
    // https://developer.android.com/develop/ui/compose/compiler
    alias(libs.plugins.kotlin.compose) apply false
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