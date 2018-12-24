plugins {
    kotlin("jvm") version Kotlin.version
    application
}

repositories {
    maven(url = "https://dl.bintray.com/kotlin/kotlin-eap/") {
        name = "Kotlin EAP"
    }
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib", Kotlin.version))
    implementation(kotlin("script-util", Kotlin.version))
    implementation(kotlin("scripting-jvm", Kotlin.version))
    implementation(kotlin("scripting-jvm-host", Kotlin.version))

    // not strictly necessary
    implementation(kotlin("reflect", Kotlin.version))
}

application {
    mainClassName = "example.MainKt"
}

val run = tasks.getByName<JavaExec>("run") {
    args = listOf("hello.example.kts")
    workingDir = File("run")
}