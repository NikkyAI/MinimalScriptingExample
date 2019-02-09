package example.annotations

@Target(AnnotationTarget.FILE)
@Repeatable
@Retention(AnnotationRetention.SOURCE)
annotation class DefaultTest(
    val a: Int = 42,
    val b: String = ""
)