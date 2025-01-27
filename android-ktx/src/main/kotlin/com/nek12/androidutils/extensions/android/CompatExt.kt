package com.nek12.androidutils.extensions.android

import android.Manifest
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresPermission

@Deprecated(Migration)
@Suppress("DEPRECATION")
@RequiresPermission(android.Manifest.permission.VIBRATE)
fun Vibrator.vibrateCompat(lengthMs: Long) {
    when (Build.VERSION.SDK_INT) {
        Build.VERSION_CODES.TIRAMISU -> vibrate(
            VibrationEffect.createOneShot(lengthMs, VibrationEffect.DEFAULT_AMPLITUDE),
            VibrationAttributes.createForUsage(VibrationAttributes.USAGE_ALARM)
        )

        Build.VERSION_CODES.Q -> vibrate(VibrationEffect.createOneShot(lengthMs, VibrationEffect.DEFAULT_AMPLITUDE))
        else -> vibrate(lengthMs)
    }
}

@Deprecated(Migration)
@Suppress("DEPRECATION")
@RequiresPermission(android.Manifest.permission.VIBRATE)
fun Vibrator.vibrateCompat(waveform: LongArray, repeat: Int = -1) {
    when (Build.VERSION.SDK_INT) {
        Build.VERSION_CODES.TIRAMISU -> vibrate(
            VibrationEffect.createWaveform(waveform, repeat),
            VibrationAttributes.createForUsage(VibrationAttributes.USAGE_ALARM)
        )

        Build.VERSION_CODES.Q -> vibrate(VibrationEffect.createWaveform(waveform, repeat))
        else -> vibrate(waveform, repeat)
    }
}

/**
 * Will work only if QUERY_ALL_PACKAGES permission is present
 */
@Deprecated(Migration)
@RequiresPermission(Manifest.permission.QUERY_ALL_PACKAGES)
fun PackageManager.getAppInfoCompat(packageName: String, flags: Int) = withApiLevel(
    versionCode = Build.VERSION_CODES.TIRAMISU,
    below = { getApplicationInfo(packageName, flags) },
    since = { getApplicationInfo(packageName, PackageManager.ApplicationInfoFlags.of(flags.toLong())) }
)

/**
 * Will work only if QUERY_ALL_PACKAGES permission is present
 */
@Deprecated(Migration)
fun PackageManager.queryIntentActivitiesCompat(
    intent: Intent,
    flags: Int = 0,
): List<ResolveInfo> = withApiLevel(
    versionCode = Build.VERSION_CODES.TIRAMISU,
    below = { queryIntentActivities(intent, flags) },
    since = { queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(flags.toLong())) },
)

@Deprecated(Migration)
fun PackageManager.getInstalledPackagesCompat(
    flags: Int = 0,
): List<PackageInfo> = withApiLevel(
    versionCode = Build.VERSION_CODES.TIRAMISU,
    below = { getInstalledPackages(flags) },
    since = { getInstalledPackages(PackageManager.PackageInfoFlags.of(flags.toLong())) }
)
