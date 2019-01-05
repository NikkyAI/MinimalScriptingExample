package example.script

import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.compilerOptions
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm

class ScriptEnvironmentConfiguration : ScriptCompilationConfiguration({
    defaultImports.append(
        "example.Constants"
    )
    jvm {
        // ensures that all dependencies are available to the script
        dependenciesFromCurrentContext()
    }
    // still broken ?
//    compilerOptions.append("-jvm-target", "1.8")
})


