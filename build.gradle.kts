import org.apache.tools.ant.util.JavaEnvUtils
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Kotlin.version
    application
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        apiVersion = "1.3"
        languageVersion = "1.3"
        jvmTarget = "1.8"
        freeCompilerArgs = listOf(
            "-XXLanguage:+InlineClasses",
            "-progressive"
        )
    }
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib", Kotlin.version))

    // script definition
    implementation(kotlin("scripting-jvm", Kotlin.version))

    // host
    implementation(kotlin("script-util", Kotlin.version))
    implementation(kotlin("scripting-jvm-host-embeddable", Kotlin.version))

    // not strictly necessary
    implementation(kotlin("reflect", Kotlin.version))
}

application {
    mainClassName = "example.MainKt"
}

//val javac = File(JavaEnvUtils.getJdkExecutable("javac"))
//val jdkHome = javac.parentFile.parentFile
//logger.lifecycle("jdkHome: $jdkHome")

val run = tasks.getByName<JavaExec>("run") {
    args = listOf("hello.example.kts")
    workingDir = File("run")
//    systemProperty("jdkHome", jdkHome.path)
}