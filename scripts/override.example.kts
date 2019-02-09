@file:Import("TestInclude.kt")

//override fun doThings(id: String) {
//    println("doWork: $id")
//}

/*
does not compile with override specified

ScriptDiagnostic(message=Modifier 'override' is not applicable to 'local function', severity=ERROR, sourcePath=/home/nikky/dev/MinimalScriptingExample/run/./hello.example.kts, location=Location(start=Position(line=13, col=1, absolutePos=null), end=null), exception=null)
e: /home/nikky/dev/MinimalScriptingExample/run/./override.example.kts: (13, 1): Modifier 'override' is not applicable to 'local function'
 */