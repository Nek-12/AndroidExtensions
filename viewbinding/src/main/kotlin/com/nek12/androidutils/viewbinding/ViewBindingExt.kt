package com.nek12.androidutils.viewbinding

import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding

internal const val Deprecation = """
    XML is deprecated.
    The maintainer team no longer uses XML and cannot provide support for this module anymore.
    Please either: 
      - Migrate to Compose and use KMPUtils https://github.com/respawn-app/KMPUtils
      - Copy and paste these extensions into your project
"""

@Deprecated(Deprecation)
fun ViewBinding.getString(@StringRes id: Int) = root.context.getString(id)

@Deprecated(Deprecation)
fun ViewBinding.getString(@StringRes id: Int?) = id?.let { getString(it) }

@Deprecated(Deprecation)
fun ViewBinding.getString(@StringRes id: Int, vararg args: Any?) = root.context.getString(id, *args)

@Deprecated(Deprecation)
fun ViewBinding.dimension(@DimenRes dimen: Int) = root.resources.getDimension(dimen).toInt()

@Deprecated(Deprecation)
fun ViewBinding.drawable(@DrawableRes drawable: Int) = ContextCompat.getDrawable(root.context, drawable)!!

@Deprecated(Deprecation)
fun ViewBinding.getAttribute(@AttrRes attr: Int): Int {
    val value = TypedValue()
    root.context.theme.resolveAttribute(attr, value, true)
    return value.data
}
