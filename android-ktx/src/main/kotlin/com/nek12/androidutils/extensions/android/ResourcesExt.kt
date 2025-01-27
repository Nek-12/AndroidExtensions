@file:Suppress("unused")

package com.nek12.androidutils.extensions.android

import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.os.ConfigurationCompat
import java.util.Locale

/**
 * Get dimension dp value from your xml.
 * When you use [Resources.getDimension] you get the amount of pixels for that dimen.
 * This function returns a proper dp value just like what you wrote in your dimen.xml
 */
@Deprecated(Migration)
fun Resources.getDimenInDP(id: Int): Int = (getDimension(id) / displayMetrics.density).toInt()

/**
 * Rescales the bitmap
 * @param maxSize The maximum size of the longest side of the image (can be either height or width) in pixels
 * @return scaled bitmap
 */
@Deprecated(Migration)
fun Bitmap.scale(maxSize: Int): Bitmap {
    val ratio = width.toFloat() / height.toFloat()
    var newWidth = maxSize
    var newHeight = maxSize
    if (ratio > 1) {
        newHeight = (maxSize / ratio).toInt()
    } else {
        newWidth = (maxSize * ratio).toInt()
    }
    return Bitmap.createScaledBitmap(this@scale, newWidth, newHeight, true)
}

/**
 * Uses the value of this int as a **resource id** to parse an [android.graphics.Color] object
 */
@Deprecated(Migration)
fun Int.asColor(context: Context) = ContextCompat.getColor(context, this)

/**
 * Uses this int as a **resource id** to get a drawable
 */
@Deprecated(Migration)
fun Int.asDrawable(context: Context) = ContextCompat.getDrawable(context, this)

@Deprecated(Migration)
val Resources.currentLocale: Locale
    get() = ConfigurationCompat.getLocales(configuration).get(0)!!

@Deprecated(Migration)
fun Context.getResourceUri(resourceId: Int): Uri = Uri.Builder()
    .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
    .authority(resources.getResourcePackageName(resourceId))
    .appendPath(resources.getResourceTypeName(resourceId))
    .appendPath(resources.getResourceEntryName(resourceId))
    .build()
