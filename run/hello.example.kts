import example.Constants

class Hello(val content: String = "World") {
    override fun toString(): String {
        return "Hello $content"
    }
}

val hello = Hello("World")

println(hello)

// this line will break
println(Constants.a)

override fun implementMe() {
    println("does not work")
}