package pw.kmp.kodeinject

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.TT
import com.github.salomonbrys.kodein.description
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.javaType

/**
 * A constructor available for injection using a Kodein instance.
 */
class InjectedConstructor<out T : Any>(val constructor: KFunction<T>, val kodein: Kodein) {

    /**
     * Instantiates the class and injects parameters into the constructor.
     */
    fun inject(): T {
        return constructor.callBy(provideParameters())
    }

    /**
     * Creates an array of parameters to pass to the constructor.
     */
    internal fun provideParameters(): Map<KParameter, Any?> {
        return constructor.parameters.map { it.to(provideParameter(it.type.javaType as Class<*>)) }.toMap()
    }

    /**
     * Fetches a parameter from the Kodein container.
     */
    internal fun <T : Any> provideParameter(type: Class<T>): T? {
        if (type == Kodein::class.java) return kodein as T
        return kodein.ProviderOrNull(TT(type))?.invoke()
    }

    companion object {

        /**
         * Creates an InjectedConstructor for a given type.
         */
        fun <T : Any> create(type: KClass<T>, kodein: Kodein): InjectedConstructor<T> {
            val constructor = type.constructors.filter { it.parameters.all { canProvide(it, kodein) }}.firstOrNull()
                    ?: throw IllegalArgumentException("No constructors of ${type.simpleName} match available bindings.\nRegistered in Kodein:\n" + kodein.container.bindings.description)
            return InjectedConstructor(constructor, kodein)
        }

        /**
         * Returns whether the Kodein instance can provide a given type.
         */
        internal fun canProvide(param: KParameter, kodein: Kodein): Boolean {
            return param.type.javaType is Class<*> &&
                    (kodein.ProviderOrNull(TT(param.type.javaType as Class<*>)) != null ||
                    param.type.javaType == Kodein::class.java ||
                    param.type.isMarkedNullable)
        }

    }

}