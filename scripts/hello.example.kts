@file:Import("TestInclude.kt")
@file:DefaultTest()

import example.annotations.DefaultTest

class Hello(val content: String = "World") {
    override fun toString(): String {
        return "Hello $content"
    }
}

val hello = Hello("World")

println(hello)

println("directory: $directory")

//coroutineTest()
