package example.script

import example.annotations.DefaultTest
import example.annotations.Import
import kotlin.script.experimental.api.ScriptCollectedData
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.script.experimental.api.asSuccess
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.api.foundAnnotations
import kotlin.script.experimental.api.importScripts
import kotlin.script.experimental.api.refineConfiguration
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import org.jetbrains.kotlin.script.InvalidScriptResolverAnnotation
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.host.FileScriptSource
import kotlin.script.experimental.host.toScriptSource

class ScriptDefinitionConfiguration : ScriptCompilationConfiguration({
    defaultImports(Import::class, DefaultTest::class)
    defaultImports.append(
        "example.Constants"
    )
    jvm {
        // ensures that all dependencies are available to the script
        dependenciesFromCurrentContext(wholeClasspath = false)
    }

    refineConfiguration {
        onAnnotations(Import::class, DefaultTest::class) { context ->
            println("on annotations")
            val scriptFile = (context.script as FileScriptSource).file
            val rootDir = scriptFile.parentFile.parentFile

            val reports = mutableListOf<ScriptDiagnostic>()
            val annotations = context.collectedData?.get(ScriptCollectedData.foundAnnotations)?.also { annotations ->
                reports += ScriptDiagnostic("annotations: $annotations", ScriptDiagnostic.Severity.INFO)

                if(annotations.any { it is InvalidScriptResolverAnnotation }) {
                    reports += ScriptDiagnostic("InvalidScriptResolverAnnotation found", ScriptDiagnostic.Severity.ERROR)
                    return@onAnnotations ResultWithDiagnostics.Failure(reports)
                }
            } ?: return@onAnnotations context.compilationConfiguration.asSuccess(reports)

            val importAnnotations = annotations.filterIsInstance(Import::class.java)
            reports += ScriptDiagnostic("importAnnotations: $importAnnotations", ScriptDiagnostic.Severity.INFO)

            val defaultAnnotations = annotations.filterIsInstance(DefaultTest::class.java)
            reports += ScriptDiagnostic("defaultAnnotations: $defaultAnnotations", ScriptDiagnostic.Severity.INFO)


            val sources = importAnnotations.map {
                rootDir.resolve("include").resolve(it.source)
            }.distinct()

            return@onAnnotations ScriptCompilationConfiguration(context.compilationConfiguration) {
                if(sources.isNotEmpty()) {
                    importScripts.append(sources.map { it.toScriptSource() })
                    reports += ScriptDiagnostic(
                        "importScripts += ${sources.map { it.relativeTo(rootDir) }}",
                        ScriptDiagnostic.Severity.INFO
                    )
                }
            }.asSuccess(reports)
        }
    }
})


