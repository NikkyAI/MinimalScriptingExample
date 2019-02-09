MinimalScriptingExample

small playground and example code for what can be done with kotlin scripting

to run the example: `./gradlew run_hello`

for updating the idea script resolving, currently idea needs o be restarted it seems


for IDE highlighting make sure a jar containing `src/main/kotlin/resources/META-INFF/kotlin/scripting/templates/example.script.ScriptEnvironment` is in the classpath
and the script file is in a sourceRoot in idea

in this sample it is resolved via publishing `script-host` to mavenLocal and
depending on it from the main project
