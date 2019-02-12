package example.annotations

import example.TestEnum

@Target(AnnotationTarget.FILE)
@Repeatable
@Retention(AnnotationRetention.SOURCE)
annotation class EnumTest(
    val enumValue: TestEnum
)