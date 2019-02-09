@file:Import("TestInclude.kt")

import example.Constants

class Hello(val content: String = "World") {
    override fun toString(): String {
        return "Hello $content"
    }
}

val hello = Hello("World")

println(hello)

//override fun doWork(id: String) {
//    println("doWork: $id")
//}

/*
does not compile with override specified

ScriptDiagnostic(message=Modifier 'override' is not applicable to 'local function', severity=ERROR, sourcePath=/home/nikky/dev/MinimalScriptingExample/run/./hello.example.kts, location=Location(start=Position(line=13, col=1, absolutePos=null), end=null), exception=null)
e: /home/nikky/dev/MinimalScriptingExample/run/./override.example.kts: (13, 1): Modifier 'override' is not applicable to 'local function'
 */