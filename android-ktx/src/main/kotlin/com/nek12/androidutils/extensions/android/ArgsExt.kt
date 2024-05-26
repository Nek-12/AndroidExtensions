package com.nek12.androidutils.extensions.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import java.io.Serializable
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

@Deprecated(Migration)
inline fun <reified T : Activity> Context.intent(vararg extras: Pair<KProperty1<T, *>, Any?>): Intent =
    intentFor<T>(this).setExtras(extras = extras)

@Deprecated(Migration)
inline fun <reified T : Any> intentFor(context: Context): Intent = Intent(context, T::class.java)

@Deprecated(Migration)
fun <T : Activity> Intent.setExtras(vararg extras: Pair<KProperty1<T, Any?>, Any?>): Intent = apply {
    putExtras(bundleOf(pairs = extras.map { it.first.name to it.second }.toTypedArray()))
}

@Deprecated(Migration)
inline fun <reified T> Activity.extra(defaultValue: T? = null) =
    object : BundleExtra<Activity, T>(null is T, defaultValue) {
        override val bundle: Bundle? get() = intent.extras
    }

@Deprecated(Migration)
@Suppress("UNCHECKED_CAST")
abstract class BundleExtra<in T, V>(
    private val isNullable: Boolean,
    private val defaultValue: V?,
) : ReadOnlyProperty<T, V> {

    abstract val bundle: Bundle?

    private var internalValue: Any? = UNINITIALIZED_VALUE

    // this relies on type-unsafe api
    @Suppress("DEPRECATION", "UseRequire")
    override fun getValue(thisRef: T, property: KProperty<*>): V = when (internalValue) {
        is UNINITIALIZED_VALUE -> {
            internalValue = bundle?.get(property.name)
                ?: defaultValue
                        ?: if (isNullable)
                    null
                else
                    throw IllegalArgumentException("Required value was not provided: ${property.name}")
        }
        else -> internalValue
    } as V
}

@Deprecated(Migration)
@Suppress("ClassName")
private object UNINITIALIZED_VALUE

@Deprecated(Migration)
@Suppress("DEPRECATION")
inline fun <reified T : Serializable> Bundle.serializable(key: String) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        getSerializable(key, T::class.java)
    else getSerializable(key) as? T?

@Deprecated(Migration)
inline fun <reified T : Serializable> Intent.serializable(key: String): T? = extras?.serializable(key)

@Deprecated(Migration)
inline fun <reified T : Serializable> Bundle.requireSerializable(key: String): T = requireNotNull(serializable(key))

@Deprecated(Migration)
inline fun <reified T : Serializable> Intent.requireSerializable(key: String): T = requireNotNull(serializable(key))

@Deprecated(Migration)
@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        getParcelable(key, T::class.java)
    else getParcelable(key) as? T?

@Deprecated(Migration)
inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = extras?.parcelable(key)

@Deprecated(Migration)
inline fun <reified T : Parcelable> Bundle.requireParcelable(key: String): T = requireNotNull(parcelable(key))

@Deprecated(Migration)
inline fun <reified T : Parcelable> Intent.requireParcelable(key: String): T = requireNotNull(parcelable(key))
