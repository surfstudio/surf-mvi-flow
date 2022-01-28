buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    }
}

plugins {
    id("com.diffplug.spotless")
    id("com.jfrog.artifactory")
    `maven-publish`
    kotlin("plugin.serialization") version "1.6.10"
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
        }
    }
}