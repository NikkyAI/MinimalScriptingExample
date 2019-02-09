pluginManagement {
    repositories {
        maven(url = "https://dl.bintray.com/kotlin/kotlin-dev/") {
            name = "Kotlin Dev"
        }
        maven(url = "https://dl.bintray.com/kotlin/kotlin-eap/") {
            name = "Kotlin EAP"
        }
        maven(url = "https://kotlin.bintray.com/kotlinx/") {
            name = "kotlinx"
        }
        mavenCentral()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlinx-serialization") {
                useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
            }
        }
    }
}
rootProject.name = "script-host"
