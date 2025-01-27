package com.nek12.androidutils.extensions.preferences

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

typealias PreferenceGetter<T> = SharedPreferences.(String, T) -> T
typealias PreferenceSetter<T> = SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor

/**
 * A sharedPreferences delegate that allows you to write one-liners for loading and saving data
 * from/to your app's default SharedPreferences.
 * Uses [SharedPreferences.Editor.apply] that does sharedpreferences operations on background thread
 * SharedPreferences is a Singleton object so you can easily get as many references as you want,
 * it opens file only when you call getSharedPreferences first time, or create only one reference for it.
 * example:
 * ```
 *  var isFirstLaunch: Boolean = booleanPreference()
 *  if (isFirstLaunch) {
 *      //...
 *  }
 *  isFirstLaunch = false
 * ```
 */
@Deprecated(Deprecation)
abstract class PreferenceProperty<in T, V>(
    private val defaultValue: V,
    private val key: String? = null,
    private val getter: PreferenceGetter<V>,
    private val setter: PreferenceSetter<V>,
) : ReadWriteProperty<T, V> {

    abstract fun getPreferences(thisRef: T): SharedPreferences

    override fun getValue(thisRef: T, property: KProperty<*>): V =
        getPreferences(thisRef).getter(key ?: property.name, defaultValue)

    @SuppressLint("CommitPrefEdits")
    override fun setValue(thisRef: T, property: KProperty<*>, value: V) =
        getPreferences(thisRef).edit().setter(key ?: property.name, value).apply()
}

@Deprecated(Deprecation)
open class DefaultPreferenceProperty<T>(
    defaultValue: T,
    key: String? = null,
    getter: PreferenceGetter<T>,
    setter: PreferenceSetter<T>,
) : PreferenceProperty<Context, T>(defaultValue, key, getter, setter) {

    private var _prefs: SharedPreferences? = null

    override fun getPreferences(thisRef: Context): SharedPreferences =
        _prefs ?: thisRef.defaultPreferences().also { _prefs = it }
}

@Deprecated(Deprecation)
open class ProvidedPreferenceProperty<T>(
    defaultValue: T,
    key: String? = null,
    getter: PreferenceGetter<T>,
    setter: PreferenceSetter<T>,
    private val preferences: SharedPreferences,
) : PreferenceProperty<Any?, T>(defaultValue, key, getter, setter) {

    override fun getPreferences(thisRef: Any?): SharedPreferences = preferences
}
