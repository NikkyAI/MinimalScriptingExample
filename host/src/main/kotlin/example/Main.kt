package example

import example.host.createJvmScriptingHost
import example.host.evalScript
import example.script.ScriptDefinition
import java.io.File
import kotlin.system.exitProcess

fun main(vararg args: String) {

    val scriptPath = args.getOrNull(0) ?: run {
        System.err.println("no script file passed")
        exitProcess(-1)
    }

    // use a cacheDir in the OS specific directories in production PLEASE
    val cacheDir = File(System.getProperty("user.dir")).resolve(".cache")
    cacheDir.mkdirs()

    val host = createJvmScriptingHost(cacheDir)
    val scriptFile = File(System.getProperty("user.dir")).resolve(scriptPath).absoluteFile

    val workingDir = File(System.getProperty("user.dir")).absoluteFile!!
    val scriptEnv = host.evalScript<ScriptDefinition>(scriptFile, args = *arrayOf(workingDir))

    println("\n")
    println("scriptEnv: $scriptEnv")
    println("\n")

    val id = scriptFile.name.substringBeforeLast(".example.kts")
    scriptEnv.doThings(id)
}