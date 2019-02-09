import org.apache.tools.ant.util.JavaEnvUtils
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Kotlin.version
    kotlin("plugin.scripting") version Kotlin.version
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
    mavenLocal()
    mavenCentral()
    jcenter()
}


dependencies {
//    implementation(kotlin("stdlib", Kotlin.version))
//    api(files(shadowJar.archiveFile))
    implementation(group = "moe.nikky", name = "script-host", version = "1.0-SNAPSHOT")
}

val buildHost = task<GradleBuild>("buildHost") {
    tasks = listOf("host:publishToMavenLocal", "host:shadowJar")
    buildFile = rootDir.parentFile.resolve("build.gradle.kts")
    dir = rootDir.resolve("host")
}

val jarFile = rootDir.parentFile.resolve("host")
    .resolve("build").resolve("libs")
    .resolve("script-host.jar")

val runDir = rootDir.resolve("scripts")

runDir
    .listFiles { _, name -> name.endsWith(".example.kts") }
    .forEach { scriptFile ->
        val id = scriptFile.name.substringBeforeLast(".example.kts")
        task<JavaExec>("run_$id") {
            dependsOn(buildHost)
            group = "application"
            args = listOf(scriptFile.name)
            workingDir = runDir
            main = "example.MainKt"
            classpath(jarFile)
        }
    }

kotlin {
    sourceSets.maybeCreate("main").kotlin.apply {
        srcDir(runDir)
        srcDir(rootDir.resolve("include"))
    }
}


task<DefaultTask>("depsize") {
    group = "help"
    description = "prints dependency sizes"
    doLast {
        val formatStr = "%,10.2f"
        val size = configurations.default.get().resolve()
            .map { it.length() / (1024.0 * 1024.0) }.sum()

        val out = buildString {
            append("Total dependencies size:".padEnd(45))
            append("${String.format(formatStr, size)} Mb\n\n")
            configurations
                .default
                .get()
                .resolve()
                .sortedWith(compareBy { -it.length() })
                .forEach {
                    append(it.name.padEnd(45))
                    append("${String.format(formatStr, (it.length() / 1024.0))} kb\n")
                }
        }
        println(out)
    }
}