@file:Import("TestInclude.kt")
@file:DefaultTest()

import example.annotations.DefaultTest

class HelloWorld(val content: String = "World") {
    override fun toString(): String {
        return "Hello $content"
    }
}

println("directory: $directory")

val foo = HelloWorld("World")
val bar = "World"

println(foo)
println("Hello $bar")

// no override ???
fun doThings(id: String) {
    println("custom implementation of `doThings`")
    println("scriptfile id is '$id'")
}
