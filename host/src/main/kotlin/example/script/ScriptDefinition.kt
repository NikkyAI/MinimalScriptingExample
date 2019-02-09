package example.script

import example.ID
import java.io.File
import kotlin.script.experimental.annotations.KotlinScript

@KotlinScript(
    displayName = "Example Script",
    fileExtension = "example.kts",
    compilationConfiguration = ScriptDefinitionConfiguration::class
)
open class ScriptDefinition(
    val directory: File // set the constructor arguments in the compilation configuration
) {
    override fun toString() = "ScriptEnvironment(directory = $directory) is a '${this::class.qualifiedName}'"

    open operator fun ID.unaryPlus(): ID {
        println(this)
        return this
    }

    open fun doThings(id: String) {
        //TODO("please override doThings(id: String)")
        println("default doThings(\"$id\")")
    }
}