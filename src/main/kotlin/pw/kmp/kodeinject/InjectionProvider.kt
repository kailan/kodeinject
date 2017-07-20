package pw.kmp.kodeinject

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.TT
import com.github.salomonbrys.kodein.TypeToken
import com.github.salomonbrys.kodein.bindings.NoArgBinding
import com.github.salomonbrys.kodein.bindings.NoArgBindingKodein
import com.github.salomonbrys.kodein.description
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaType

/**
 * Provides a factory that performs dependency injection into constructors.
 */
open class InjectionProvider<T : Any>(val type: KClass<T>, final override val createdType: TypeToken<T>) : NoArgBinding<T> {

    override fun getInstance(kodein: NoArgBindingKodein, key: Kodein.Key<Unit, T>): T {
        val constructor = InjectedConstructor.create(type, kodein)
        return constructor.inject()
    }

    override fun factoryName() = "injected"
    override val description: String get() = "${factoryName()}<${createdType.simpleDispString()}>()"
    override val fullDescription: String get() = "${factoryName()}<${createdType.fullDispString()}>()"

}

/**
 * An injection provider that only creates a single instance of the type.
 */
class SingletonInjectionProvider<T : Any>(type: KClass<T>, createdType: TypeToken<T>) : InjectionProvider<T>(type, createdType) {

    var instance: T? = null

    override fun getInstance(kodein: NoArgBindingKodein, key: Kodein.Key<Unit, T>): T {
        if (instance != null) return instance!!
        instance = super.getInstance(kodein, key)
        return instance!!
    }

    override fun factoryName() = "injectedSingleton"

}