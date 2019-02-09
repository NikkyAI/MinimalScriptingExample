@file:Import("TestInclude.kt")
@file:DefaultTest(a = 2)

import example.annotations.DefaultTest

class Hello(val content: String = "World") {
    override fun toString(): String {
        return "Hello $content"
    }
}

val hello = Hello("World")

println(hello)

//fun doThings(id: String) {
//    println("my id is `$id`")
//}

println("directory: $directory")