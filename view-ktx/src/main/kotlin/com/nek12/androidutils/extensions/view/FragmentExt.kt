package com.nek12.androidutils.extensions.view

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.nek12.androidutils.extensions.android.BundleExtra
import com.nek12.androidutils.extensions.android.Email
import com.nek12.androidutils.extensions.android.autofillManager
import com.nek12.androidutils.extensions.android.dialNumber
import com.nek12.androidutils.extensions.android.downloadFile
import com.nek12.androidutils.extensions.android.openBrowser
import com.nek12.androidutils.extensions.android.sendEmail
import com.nek12.androidutils.extensions.android.shareAsText
import com.nek12.androidutils.extensions.android.startActivityCatching
import kotlin.reflect.KProperty0

/**
 * Whether the device is in landscape mode right now
 */
@Deprecated(Deprecation)
val Fragment.isLandscape: Boolean
    get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

@Deprecated(Deprecation)
fun Fragment.doOnBackPressed(action: OnBackPressedCallback) {
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, action)
}

@Deprecated(Deprecation)
fun Fragment.doOnBackPressed(action: OnBackPressedCallback.() -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, true, action)
}

@Deprecated(Deprecation)
fun Fragment.sendEmail(mail: Email, onNotFound: (e: Exception) -> Unit) =
    requireContext().sendEmail(mail, onNotFound)

@Deprecated(Deprecation)
fun Fragment.sendEmail(uri: Uri, onNotFound: (e: Exception) -> Unit) = sendEmail(Email.ofUri(uri), onNotFound)

@Deprecated(Deprecation)
fun Fragment.shareAsText(text: String, onAppNotFound: (e: Exception) -> Unit) =
    requireContext().shareAsText(text, onAppNotFound)

@Deprecated(Deprecation)
fun Fragment.openBrowser(url: Uri, onAppNotFound: (e: Exception) -> Unit) =
    requireContext().openBrowser(url, onAppNotFound)

@Deprecated(Deprecation)
fun Fragment.downloadFile(
    url: Uri,
    fileName: String,
    userAgent: String? = null,
    description: String? = null,
    mimeType: String? = null,
    onAppNotFound: (e: Exception) -> Unit,
) {
    requireContext().downloadFile(url, fileName, userAgent, description, mimeType, onAppNotFound)
}

@Deprecated(Deprecation)
fun Fragment.startActivityCatching(intent: Intent, onNotFound: (Exception) -> Unit) =
    requireContext().startActivityCatching(intent, onNotFound)

@Deprecated(Deprecation)
fun Fragment.dialNumber(numberUri: Uri, onNotFound: (e: Exception) -> Unit) =
    requireContext().dialNumber(numberUri, onNotFound)

@Deprecated(Deprecation)
val Fragment.autofillManager get() = requireContext().autofillManager

@Deprecated(Deprecation)
val Fragment.screenWidthPx get() = requireActivity().resources.displayMetrics.widthPixels

@Deprecated(Deprecation)
val Fragment.screenHeightPx get() = requireActivity().resources.displayMetrics.heightPixels

/**
 * @see [android.util.DisplayMetrics.density]
 */
@Deprecated(Deprecation)
val Fragment.screenDensity get() = requireActivity().resources.displayMetrics.density

fun <T : Fragment> T.setArgs(vararg args: Pair<KProperty0<Any?>, Any?>): T = apply {
    arguments = bundleOf(pairs = args.map { it.first.name to it.second }.toTypedArray())
}

@Deprecated(Deprecation)
inline fun <reified T> Fragment.arg(defaultValue: T? = null) =
    object : BundleExtra<Fragment, T>(null is T, defaultValue) {
        override val bundle: Bundle? get() = arguments
    }
