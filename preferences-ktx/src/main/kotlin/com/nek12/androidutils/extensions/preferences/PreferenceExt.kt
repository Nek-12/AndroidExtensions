@file:Suppress("unused")

package com.nek12.androidutils.extensions.preferences

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import java.time.Instant
import kotlin.properties.ReadWriteProperty

internal const val Deprecation = """
Shared preferences are deprecated - please migrate from shared preferences as they read files on main thread and cause
a significant number of ANRs. Preferences are also not type safe and do not handle errors.
By using these delegates, you cannot avoid the problems above.
Use recommended solutions for data storage such as Json / Protobuf DataStore or a database.
"""

/**
 * Obtains default shared preferences for this application
 */
@Deprecated(Deprecation)
fun Context.defaultPreferences(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

@Deprecated(Deprecation)
fun intPreference(
    preferences: SharedPreferences,
    defaultValue: Int = 0,
    key: String? = null,
): ReadWriteProperty<Any?, Int> = ProvidedPreferenceProperty(
    defaultValue,
    key,
    SharedPreferences::getInt,
    SharedPreferences.Editor::putInt,
    preferences
)

@Deprecated(Deprecation)
fun stringPreference(
    preferences: SharedPreferences,
    defaultValue: String? = null,
    key: String? = null,
): ReadWriteProperty<Any?, String?> = ProvidedPreferenceProperty(
    defaultValue,
    key,
    SharedPreferences::getString,
    SharedPreferences.Editor::putString,
    preferences
)

@Deprecated(Deprecation)
fun booleanPreference(
    preferences: SharedPreferences,
    defaultValue: Boolean = false,
    key: String? = null,
): ReadWriteProperty<Any?, Boolean> = ProvidedPreferenceProperty(
    defaultValue,
    key,
    SharedPreferences::getBoolean,
    SharedPreferences.Editor::putBoolean,
    preferences
)

@Deprecated(Deprecation)
fun floatPreference(
    preferences: SharedPreferences,
    defaultValue: Float = 0f,
    key: String? = null,
): ReadWriteProperty<Any?, Float> = ProvidedPreferenceProperty(
    defaultValue,
    key,
    SharedPreferences::getFloat,
    SharedPreferences.Editor::putFloat,
    preferences
)

@Deprecated(Deprecation)
fun longPreference(
    preferences: SharedPreferences,
    defaultValue: Long = 0L,
    key: String? = null,
): ReadWriteProperty<Any?, Long> = ProvidedPreferenceProperty(
    defaultValue,
    key,
    SharedPreferences::getLong,
    SharedPreferences.Editor::putLong,
    preferences
)

/**
 * @return [defaultValue] if base [SharedPreferences.getString] returned `null`
 */
@Deprecated(Deprecation)
@JvmName("stringPreferenceNotNull")
fun stringPreference(
    preferences: SharedPreferences,
    defaultValue: String,
    key: String? = null,
): ReadWriteProperty<Any?, String> = ProvidedPreferenceProperty(
    defaultValue,
    key,
    { k, default -> getString(k, default) ?: defaultValue },
    SharedPreferences.Editor::putString,
    preferences
)

@Deprecated(Deprecation)
@SuppressLint("NewApi")
fun instantPreference(
    preferences: SharedPreferences,
    defaultValue: Instant,
    key: String? = null,
) = ProvidedPreferenceProperty(
    defaultValue = defaultValue,
    key = key,
    setter = { k, v -> putLong(k, v.toEpochMilli()) },
    getter = { k, v -> Instant.ofEpochMilli(getLong(k, v.toEpochMilli())) },
    preferences = preferences,
)

@Deprecated(Deprecation)
inline fun <reified T : Enum<T>> enumStringPreference(
    preferences: SharedPreferences,
    defaultValue: T,
    key: String? = null,
) = ProvidedPreferenceProperty(
    defaultValue = defaultValue,
    key = key,
    getter = { k, def -> getString(k, def.name)?.let { enumValueOf<T>(it) } ?: def },
    setter = { k, v -> putString(k, v.name) },
    preferences = preferences,
)

// ------------------  Default preferences

@Deprecated(Deprecation)
fun intPreference(
    defaultValue: Int = 0,
    key: String? = null,
): ReadWriteProperty<Context, Int> = DefaultPreferenceProperty(
    defaultValue,
    key,
    SharedPreferences::getInt,
    SharedPreferences.Editor::putInt
)

@Deprecated(Deprecation)
fun stringPreference(
    defaultValue: String? = null,
    key: String? = null,
): ReadWriteProperty<Context, String?> = DefaultPreferenceProperty(
    defaultValue,
    key,
    SharedPreferences::getString,
    SharedPreferences.Editor::putString,
)

@Deprecated(Deprecation)
fun booleanPreference(
    defaultValue: Boolean = false,
    key: String? = null,
): ReadWriteProperty<Context, Boolean> = DefaultPreferenceProperty(
    defaultValue,
    key,
    SharedPreferences::getBoolean,
    SharedPreferences.Editor::putBoolean,
)

@Deprecated(Deprecation)
fun floatPreference(
    defaultValue: Float = 0f,
    key: String? = null,
): ReadWriteProperty<Context, Float> = DefaultPreferenceProperty(
    defaultValue,
    key,
    SharedPreferences::getFloat,
    SharedPreferences.Editor::putFloat,
)

@Deprecated(Deprecation)
fun longPreference(
    defaultValue: Long = 0L,
    key: String? = null,
): ReadWriteProperty<Context, Long> = DefaultPreferenceProperty(
    defaultValue,
    key,
    SharedPreferences::getLong,
    SharedPreferences.Editor::putLong,
)

/**
 * @return [defaultValue] if base [SharedPreferences.getString] returned `null`
 */
@Deprecated(Deprecation)
@JvmName("stringPreferenceNotNull")
fun stringPreference(
    defaultValue: String,
    key: String? = null,
): ReadWriteProperty<Context, String> = DefaultPreferenceProperty(
    defaultValue,
    key,
    { k, default -> getString(k, default) ?: defaultValue },
    SharedPreferences.Editor::putString
)

@Deprecated(Deprecation)
@JvmName("enumStringPreferenceNullable")
inline fun <reified T : Enum<T>> enumStringPreference(
    preferences: SharedPreferences,
    defaultValue: T?,
    key: String? = null,
) = ProvidedPreferenceProperty(
    defaultValue = defaultValue,
    key = key,
    getter = { k, def -> getString(k, def?.name)?.let { enumValueOf<T>(it) } },
    setter = { k, v -> putString(k, v?.name) },
    preferences = preferences,
)
