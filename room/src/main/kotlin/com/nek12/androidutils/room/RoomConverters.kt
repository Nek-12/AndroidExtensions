@file:Suppress("unused")

package com.nek12.androidutils.room

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.Duration
import java.time.Instant
import java.util.Arrays

internal const val Deprecation = """
    The maintainer team no longer uses Room and cannot provide support for this module anymore.
    Please copy and paste the contents of this module into your project
"""

@RequiresApi(Build.VERSION_CODES.O)
private fun capped(instant: Instant): Instant {
    val instants = arrayOf(Instant.MIN, instant, Instant.MAX)
    Arrays.sort(instants)
    return instants[1]
}

/**
 * Type converters that provide utility conversions for you:
 * Duration, UUID, Instant
 */
@TypeConverters
@RequiresApi(Build.VERSION_CODES.O)
@Deprecated(Deprecation)
class RoomConverters {

    @TypeConverter
    fun toDuration(seconds: Long?): Duration? = seconds?.let { Duration.ofSeconds(it) }

    @TypeConverter
    fun fromDuration(value: Duration?): Long? = value?.seconds

    @TypeConverter
    fun fromInstant(instant: Instant?): Long? = instant?.let { capped(it).toEpochMilli() }

    @TypeConverter
    fun toInstant(millisSinceEpoch: Long?): Instant? = millisSinceEpoch?.let {
        Instant.ofEpochMilli(millisSinceEpoch)
    }
}
