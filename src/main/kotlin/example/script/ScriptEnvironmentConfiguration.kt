package example.script

import java.io.File
import kotlin.script.experimental.api.ScriptCollectedData
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.ScriptConfigurationRefinementContext
import kotlin.script.experimental.api.asSuccess
import kotlin.script.experimental.api.compilerOptions
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.api.foundAnnotations
import kotlin.script.experimental.api.importScripts
import kotlin.script.experimental.api.refineConfiguration
import kotlin.script.experimental.host.FileScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm

class ScriptEnvironmentConfiguration : ScriptCompilationConfiguration({
    defaultImports(Import::class)
    defaultImports.append(
        "example.Constants"
    )
    jvm {
        // ensures that all dependencies are available to the script
//        dependenciesFromCurrentContext()
    }
    // still broken ?
//    compilerOptions.append("-jvm-target", "1.8")
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
    }
})


