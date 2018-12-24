package example

import example.script.ScriptEnvironment
import java.io.File
import kotlin.script.experimental.api.ResultValue
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.script.experimental.api.ScriptEvaluationConfiguration
import kotlin.script.experimental.api.SourceCode
import kotlin.script.experimental.api.compilerOptions
import kotlin.script.experimental.api.constructorArgs
import kotlin.script.experimental.api.resultOrNull
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jdkHome
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate
import kotlin.system.exitProcess

fun main(vararg args: String) {

    val scriptPath = args.getOrNull(0) ?: run {
        System.err.println("no script file passed")
        exitProcess(-1)
    }

    val config = createJvmCompilationConfigurationFromTemplate<ScriptEnvironment> {
        jvm {
            dependenciesFromCurrentContext(wholeClasspath = true)

            // this is a workaround that wll be able to be replaced with the upcoming
            // importedScripts
//            dependencies.append(
//                JvmDependency(
//                    File("/home/nikky/dev/Voodoo/samples/build/classes/kotlin/example.main")
//                )
//            )

            // you need to specify the jdk location, should usually be the same as the JAVA_HOME env variable anyway

            val JDK_HOME = System.getenv("JAVA_HOME")
                ?: throw IllegalStateException("please set JAVA_HOME to the installed jdk")
            jdkHome(File(JDK_HOME))
        }

        // once more for good measure
        compilerOptions.append("-jvm-target", "1.8")
    }
    println("config entries")
    config.entries().forEach {
        println("    $it")
    }

    val workingDir = File(".").absoluteFile

    val evaluationConfig = ScriptEvaluationConfiguration {
        constructorArgs.append(workingDir)
    }

    println("evaluationConfig entries")
    evaluationConfig.entries().forEach {
        println("    $it")
    }

    val scriptFile = File(".").resolve(scriptPath).absoluteFile
    val scriptContent = scriptFile.readText()
    val scriptSource = scriptContent.toScriptSource()
    val result = BasicJvmScriptingHost().eval(scriptSource, config, evaluationConfig)

    fun SourceCode.Location.posToString() = "(${start.line}, ${start.col})"

    for (report in result.reports) {
        println(report)
        val severityIndicator = when (report.severity) {
            ScriptDiagnostic.Severity.FATAL -> "fatal"
            ScriptDiagnostic.Severity.ERROR -> "e"
            ScriptDiagnostic.Severity.WARNING -> "w"
            ScriptDiagnostic.Severity.INFO -> "i"
            ScriptDiagnostic.Severity.DEBUG -> "d"
        }
        println("$severityIndicator: $scriptFile: ${report.location?.posToString()}: ${report.message}")
        report.exception?.printStackTrace()
    }
    println(result)
    val evalResult = result.resultOrNull() ?: run {
        System.err.println("evaluation failed")
        exitProcess(1)
    }


    val resultValue = evalResult.returnValue
    println("resultValue = '${resultValue}'")
    println("resultValue::class = '${resultValue::class}'")

    when (resultValue) {
        is ResultValue.Value -> {
            println("resultValue.name = '${resultValue.name}'")
            println("resultValue.value = '${resultValue.value}'")
            println("resultValue.type = '${resultValue.type}'")

            println("resultValue.value::class = '${resultValue.value!!::class}'")

            println()

            val env = resultValue.value as ScriptEnvironment
            println(env)
        }
    }
}