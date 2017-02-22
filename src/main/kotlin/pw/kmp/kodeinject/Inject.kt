package pw.kmp.kodeinject

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.typeClass

/**
 * Creates an injection provider: each time an instance is needed, the type will be instantiated.
 */
inline fun <reified T : Any> Kodein.Builder.injected() = InjectionProvider<T>(typeClass<T>())

/**
 * Creates an injection provider: the type will only be instantiated the first time it is needed.
 */
inline fun <reified T : Any> Kodein.Builder.injectedSingleton() = SingletonInjectionProvider<T>(typeClass<T>())