package example.script

@Target(AnnotationTarget.FILE)
@Repeatable
@Retention(AnnotationRetention.SOURCE)
annotation class TestAnnotation(
        val test: String = "test default",
        val something: String = "something else",
        val lista: Array<String> = [],
        val other: String = "aaa",
        val intArray: IntArray = []
)