package example

import example.host.createJvmScriptingHost
import example.host.evalScript
import example.script.ScriptEnvironment
import java.io.File
import kotlin.system.exitProcess

fun main(vararg args: String) {

    val scriptPath = args.getOrNull(0) ?: run {
        System.err.println("no script file passed")
        exitProcess(-1)
    }

    // use a cacheDir in the OS specific directories in production PLEASE
    val cacheDir = File("..").resolve("builds").resolve(".cache")

    val host = createJvmScriptingHost(cacheDir)
    val scriptFile = File(".").resolve(scriptPath).absoluteFile

    val workingDir = File(".").absoluteFile
    val scriptEnv = host.evalScript<ScriptEnvironment>(scriptFile, args = *arrayOf(workingDir))

    println("scriptEnv: $scriptEnv")
}