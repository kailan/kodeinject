package pw.kmp.kodeinject

import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.bindings.NoArgBinding
import com.github.salomonbrys.kodein.bindings.NoArgBindingKodein
import java.lang.reflect.Constructor

/**
 * Provides a factory that performs dependency injection into constructors.
 */
open class InjectionProvider<T : Any>(final override val createdType: TypeToken<T>) : NoArgBinding<T> {

    val type = ((createdType) as JVMTypeToken).type

    override fun getInstance(kodein: NoArgBindingKodein, key: Kodein.Key<Unit, T>): T {
        if (type !is Class<*>) throw IllegalArgumentException("Kodein can only instantiate classes.")
        val constructor = type.constructors
                .filter { it.parameterTypes.all { t -> kodein.container.bindings.keys.any { (it.bind.type as JVMTypeToken).type == t } } }
                .firstOrNull() ?: throw IllegalArgumentException("No constructors of ${type.name} match available bindings.\nRegistered in Kodein:\n" + kodein.container.bindings.description)

        val args = constructor.parameterTypes.map { kodein.container.nonNullProvider(Kodein.Bind(TypeTypeToken<Any>(it), null)).invoke() }
        return (constructor as Constructor<T>).newInstance(*args.toTypedArray())
    }

    override fun factoryName() = "injected"
    override val description: String get() = "${factoryName()}<${createdType.simpleDispString()}>()"
    override val fullDescription: String get() = "${factoryName()}<${createdType.fullDispString()}>()"

}

/**
 * An injection provider that only creates a single instance of the type.
 */
class SingletonInjectionProvider<T : Any>(createdType: TypeToken<T>) : InjectionProvider<T>(createdType) {

    var instance: T? = null

    override fun getInstance(kodein: NoArgBindingKodein, key: Kodein.Key<Unit, T>): T {
        if (instance != null) return instance!!
        instance = super.getInstance(kodein, key)
        return instance!!
    }

    override fun factoryName() = "injectedSingleton"

}