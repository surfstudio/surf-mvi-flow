plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
    id("com.jfrog.artifactory")
}

val composeVersion: String by project

// lib info
val libVersion: String by project
val libGroup: String by project

publishing {
    publications {
        register("aar", MavenPublication::class) {
            version = libVersion
            groupId = libGroup
            artifactId = project.name
            artifact("$buildDir/outputs/aar/mvi-core-$libVersion-release.aar")
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

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 23
        targetSdk = 31
        setProperty("archivesBaseName", "mvi-core-$libVersion")
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
}