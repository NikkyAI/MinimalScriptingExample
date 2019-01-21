package example.script

import java.io.File
import kotlin.script.experimental.annotations.KotlinScript

@KotlinScript(
    displayName = "Example Script",
    fileExtension = "example.kts",
    compilationConfiguration = ScriptEnvironmentConfiguration::class
)
abstract class ScriptEnvironment(
    val directory: File // set the constructor arguments in the compilation configuration
) {
    override fun toString() = "ScriptEnvironment(directory = $directory) is a '${this::class.qualifiedName}'"

    abstract fun implementMe()
}