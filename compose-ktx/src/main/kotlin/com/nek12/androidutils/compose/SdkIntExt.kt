package com.nek12.androidutils.compose

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

@Deprecated(Migration)
@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
val supportsBlur get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

@Deprecated(Migration)
@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
val supportsDynamicColors get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

@Deprecated(Migration)
@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
val supportsShaders get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
