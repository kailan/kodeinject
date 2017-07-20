package pw.kmp.kodeinject.annotations

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
/**
 * Marks a parameter that will not cause injection to fail if it is null.
 */
annotation class OrNull