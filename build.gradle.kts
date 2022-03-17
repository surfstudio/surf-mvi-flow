import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    }
}

plugins {
    id("com.diffplug.spotless")
    id("com.jfrog.artifactory")
    `maven-publish`
    kotlin("plugin.serialization") version "1.6.10"
    id("com.github.ben-manes.versions")
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

allprojects {
    // lib info
    val libVersion: String by project
    val libGroup: String by project
    val artifactName = project.name

    extra["libraryConfig"] = {
        publishing {
            publications {
                register("aar", MavenPublication::class) {
                    version = libVersion
                    groupId = libGroup
                    artifactId = artifactName
                    artifact("$buildDir/outputs/aar/$artifactName-$libVersion-release.aar")
                }
            }
        }

        artifactory {
            setContextUrl("https://artifactory.surfstudio.ru/artifactory")
            publish {
                repository {
                    setRepoKey("libs-release-local")
                    setUsername(System.getenv("surf_maven_username"))
                    setPassword(System.getenv("surf_maven_password"))
                }
                defaults {
                    publications("aar")
                    setPublishArtifacts(true)
                }
            }
        }
    }
    extra["androidConfig"] = { ex: Any ->
        (ex as? com.android.build.gradle.LibraryExtension)?.apply {
            compileSdk = 31

            defaultConfig {
                minSdk = 23
                targetSdk = 31
                setProperty("archivesBaseName", "$artifactName-$libVersion")
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildTypes {
                release {
                    isMinifyEnabled = false
                }
            }

            publishing {
                singleVariant("release") {
                    withSourcesJar()
                    withJavadocJar()
                }
            }
        }
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}