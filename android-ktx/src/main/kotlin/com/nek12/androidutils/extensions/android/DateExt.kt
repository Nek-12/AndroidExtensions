package com.nek12.androidutils.extensions.android

import android.text.format.DateUtils
import java.util.Date

@Deprecated(Migration)
val Date.isToday get() = DateUtils.isToday(time)

@Deprecated(Migration)
val Date.isYesterday get() = DateUtils.isToday(time + DateUtils.DAY_IN_MILLIS)

@Deprecated(Migration)
val Date.isTomorrow get() = DateUtils.isToday(time - DateUtils.DAY_IN_MILLIS)
