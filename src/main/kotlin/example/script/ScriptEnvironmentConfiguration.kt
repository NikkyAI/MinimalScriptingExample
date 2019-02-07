package example.script

import java.io.File
import kotlin.script.experimental.api.ScriptCollectedData
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.ScriptConfigurationRefinementContext
import kotlin.script.experimental.api.asSuccess
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.api.foundAnnotations
import kotlin.script.experimental.api.importScripts
import kotlin.script.experimental.api.refineConfiguration
import kotlin.script.experimental.host.FileScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm

class ScriptEnvironmentConfiguration : ScriptCompilationConfiguration({
    defaultImports(Import::class, TestAnnotation::class)
    defaultImports.append(
        "example.Constants"
    )
    jvm {
        // ensures that all dependencies are available to the script
        dependenciesFromCurrentContext(wholeClasspath = false)
    }

    refineConfiguration {
        onAnnotations(Import::class) { context: ScriptConfigurationRefinementContext ->
            val sources = context.collectedData?.get(ScriptCollectedData.foundAnnotations)
                ?.flatMap {
                    (it as? Import)?.sources?.map { sourceName -> FileScriptSource(File(File(".").absoluteFile, sourceName)) } ?: emptyList()
                }
                ?.takeIf { it.isNotEmpty() }
                ?: return@onAnnotations context.compilationConfiguration.asSuccess()
            ScriptCompilationConfiguration(context.compilationConfiguration) {
                importScripts.append(sources)
            }.asSuccess()
        }

        onAnnotations(TestAnnotation::class) { context ->
            val annotations = context.collectedData?.get(ScriptCollectedData.foundAnnotations) ?: return@onAnnotations context.compilationConfiguration.asSuccess()

            val list = mutableListOf<TestAnnotation>()

            for (an in annotations) {
                println(an)
                when(an) {
                    is TestAnnotation -> {
                        list.add(an)
                        println(an.toString())
                    }
                    else -> {
                        (an::class.members.find { it.name == "error" }?.call(an) as Exception?)?.printStackTrace() // from InvalidScriptAnnotation
                    }

                }
            }

            if(list.isNotEmpty()) {
                return@onAnnotations ScriptCompilationConfiguration(context.compilationConfiguration) {

                }.asSuccess()
            }

            return@onAnnotations context.compilationConfiguration.asSuccess()
        }
    }
})


