import example.Constants

class Hello(val content: String = "World") {
    override fun toString(): String {
        return "Hello $content"
    }
}

val hello = Hello("World")

println(hello)

fun doThings(id: String) {
    println("doWork: $id")
}

/*
works **without** a override specified,
IDE cannot help with autocompletion or check correctness
 */