package pw.kmp.kodeinject

import com.github.salomonbrys.kodein.*
import java.lang.reflect.Type

/**
 * Provides a factory that performs dependency injection into constructors.
 */
open class InjectionProvider<out T : Any>(override val createdType: Type, val name: String = "injected") : Provider<T> {

    override fun getInstance(kodein: ProviderKodein, key: Kodein.Key): T {
        if (createdType !is Class<*>) throw IllegalArgumentException("Kodein can only instantiate classes.")
        val clazz = createdType as Class<*>
        val constructor = clazz.constructors
                .filter { it.parameterTypes.all { t -> kodein.container.bindings.keys.any { it.bind.type == t } } }
                .firstOrNull() ?: throw IllegalArgumentException("No constructors of ${clazz.name} match available bindings.\nRegistered in Kodein:\n" + kodein.container.bindings.description)
        val args = constructor.parameterTypes.map { kodein.container.nonNullProvider(Kodein.Bind(it, null)).invoke() }
        return constructor.newInstance(*args.toTypedArray()) as T
    }

    override fun factoryName() = name
    override val description: String get() = "${factoryName()}<${createdType.simpleDispString}>()"
    override val fullDescription: String get() = "${factoryName()}<${createdType.fullDispString}>()"

}

/**
 * An injection provider that only creates a single instance of the type.
 */
class SingletonInjectionProvider<T : Any>(createdType: Type) : InjectionProvider<T>(createdType, "injectedSingleton") {

    var instance: T? = null

    override fun getInstance(kodein: ProviderKodein, key: Kodein.Key): T {
        if (instance != null) return instance!!
        instance = super.getInstance(kodein, key)
        return instance!!
    }

}