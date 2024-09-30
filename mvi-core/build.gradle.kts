plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    `maven-publish`
    alias(libs.plugins.artifactory)
    alias(libs.plugins.kotlin.compose)
}

// lib info
val libVersion: String by project
val libGroup: String by project

publishing {
    publications {
        register("aar", MavenPublication::class) {
            version = libVersion
            groupId = libGroup
            artifactId = project.name
            artifact("${layout.buildDirectory}/outputs/aar/mvi-core-$libVersion-release.aar")
        }
    }
}

artifactory {
    setContextUrl("https://artifactory.surfstudio.ru/artifactory")
    publish {
        repository {
            repoKey = "libs-release-local"
            username = System.getenv("surf_maven_username")
            password = System.getenv("surf_maven_password")
        }
        defaults {
            publications("aar")
            setPublishArtifacts(true)
        }
    }
}

android {
    namespace = "ru.surfstudio.mvi.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        targetSdk = 35
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.runtime)
    api(libs.kotlinx.coroutines.android)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.logging.timber)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockito.inline)
    testImplementation(libs.kotlinx.coroutines.test)
}