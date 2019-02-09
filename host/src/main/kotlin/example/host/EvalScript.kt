package example.host

import java.io.File
import kotlin.script.experimental.api.EvaluationResult
import kotlin.script.experimental.api.ResultValue
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.script.experimental.api.ScriptEvaluationConfiguration
import kotlin.script.experimental.api.SourceCode
import kotlin.script.experimental.api.constructorArgs
import kotlin.script.experimental.api.importScripts
import kotlin.script.experimental.api.resultOrNull
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.defaultJvmScriptingHostConfiguration
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jdkHome
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptEvaluator
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.JvmScriptCompiler
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate
import kotlin.system.exitProcess

fun createJvmScriptingHost(cacheDir: File): BasicJvmScriptingHost {
    val cache = FileBasedScriptCache(cacheDir)
    val compiler = JvmScriptCompiler(defaultJvmScriptingHostConfiguration, cache = cache)
    val evaluator = BasicJvmScriptEvaluator()
    val host = BasicJvmScriptingHost(compiler = compiler, evaluator = evaluator)
    return host
}

inline fun <reified T: Any> BasicJvmScriptingHost.evalScript(
    scriptFile: File,
    vararg args: Any?,
    importScripts: List<SourceCode> = listOf(),
    compilationConfig: ScriptCompilationConfiguration = createJvmCompilationConfigurationFromTemplate<T> {
        jvm {
            // when yyou can run your script-host from a fat jar, you can set this to
            // `wholeClasspath = false` to reduce dependencies and speed up script compilation
            dependenciesFromCurrentContext(wholeClasspath = true)

            importScripts(importScripts)

//            val JDK_HOME = System.getProperty("jdkHome") ?: System.getenv("JAVA_HOME")
//                ?: throw IllegalStateException("please pass -DjdkHome=path/to/jdk or please set JAVA_HOME to the installed jdk")
//            jdkHome(File(JDK_HOME))
        }
    }
): T {
    println("compilationConfig entries")
    compilationConfig.entries().forEach {
        println("    $it")
    }

    val evaluationConfig = ScriptEvaluationConfiguration {
        constructorArgs.append(*args)
    }

    println("evaluationConfig entries")
    evaluationConfig.entries().forEach {
        println("    $it")
    }

    val scriptSource = scriptFile.toScriptSource()

    println("compiling script, please be patient")
    val result = eval(scriptSource, compilationConfig, evaluationConfig)

    return result.get<T>(scriptFile)
}

fun SourceCode.Location.posToString() = "(${start.line}, ${start.col})"

inline fun <reified T> ResultWithDiagnostics<EvaluationResult>.get(scriptFile: File): T {

    for (report in reports) {
        println(report)
        val severityIndicator = when (report.severity) {
            ScriptDiagnostic.Severity.FATAL -> "fatal"
            ScriptDiagnostic.Severity.ERROR -> "e"
            ScriptDiagnostic.Severity.WARNING -> "w"
            ScriptDiagnostic.Severity.INFO -> "i"
            ScriptDiagnostic.Severity.DEBUG -> "d"
        }
        println("$severityIndicator: ${report.sourcePath}: ${report.location?.posToString()}: ${report.message}")
        report.exception?.apply {
            println("exception: $message")
            printStackTrace()
            this.cause?.apply {
                println("cause: $message")
                printStackTrace()
            }
            this.suppressed.forEach {
                println("suppressed exception: ${it.message}")
                it.printStackTrace()
            }
        }
    }
    println(this)
    val evalResult = resultOrNull() ?: run {
        System.err.println("evaluation failed")
        exitProcess(1)
    }

    val resultValue = evalResult.returnValue
    println("resultValue = '$resultValue'")
    println("resultValue::class = '${resultValue::class}'")

    return when (resultValue) {
        is ResultValue.Value -> {
            println("resultValue.name = '${resultValue.name}'")
            println("resultValue.value = '${resultValue.value}'")
            println("resultValue.type = '${resultValue.type}'")

            println("resultValue.value::class = '${resultValue.value!!::class}'")

            val env = resultValue.value as T
            println(env)
            env
        }
        is ResultValue.Unit -> {
            System.err.println("evaluation failed")
            exitProcess(-1)
        }
    }
}