package example.annotations

@Target(AnnotationTarget.FILE)
@Repeatable
@Retention(AnnotationRetention.SOURCE)
annotation class ArrayTest(
    val intArray: IntArray,
    vararg val strings: String
)