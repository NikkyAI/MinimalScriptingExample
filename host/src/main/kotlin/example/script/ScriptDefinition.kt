package example.script

import example.ID
import kotlinx.coroutines.delay
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
        println("override me in the ScriptFile")
        println("default doThings(\"$id\")")
    }

    suspend fun coroutineTest() {
        delay(10)
    }
}