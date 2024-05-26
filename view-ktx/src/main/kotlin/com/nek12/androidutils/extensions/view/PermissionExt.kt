@file:Suppress("unused")

package com.nek12.androidutils.extensions.view

import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment

@Deprecated(Deprecation)

fun Fragment.isPermissionGranted(permission: String) = context?.let {
    PermissionChecker.checkSelfPermission(it, permission) == PermissionChecker.PERMISSION_GRANTED
} ?: false

@Deprecated(Deprecation)
fun Fragment.requestPermission(permission: String, callback: ActivityResultCallback<Boolean>) {
    registerForActivityResult(ActivityResultContracts.RequestPermission(), callback).launch(permission)
}
