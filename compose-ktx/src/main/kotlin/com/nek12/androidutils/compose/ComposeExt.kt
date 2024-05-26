@file:Suppress("unused")

package com.nek12.androidutils.compose

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import android.text.format.DateFormat
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nek12.androidutils.extensions.android.Text
import com.nek12.androidutils.extensions.android.Text.Dynamic
import com.nek12.androidutils.extensions.android.Text.Resource
import com.nek12.androidutils.extensions.android.string

@Deprecated(Migration)
val isSystem24Hour @Composable get() = DateFormat.is24HourFormat(LocalContext.current)

@Deprecated(Migration)
@Composable
fun Int.plural(
    quantity: Int,
    vararg args: Any,
) = pluralStringResource(this, quantity, formatArgs = args)

@Deprecated(Migration)
@Composable
fun Int?.plural(quantity: Int, vararg args: Any) = this?.plural(quantity, args)

@Deprecated(Migration)
@ExperimentalAnimationGraphicsApi
@Composable
fun Int.animatedVector() = AnimatedImageVector.animatedVectorResource(id = this)

@Deprecated(Migration)
@ExperimentalAnimationGraphicsApi
@Composable
fun Int?.animatedVector() = this?.animatedVector()

@Deprecated(Migration)
@Composable
fun Int.string(vararg args: Any) = stringResource(id = this, formatArgs = args)

@Deprecated(Migration)
@Composable
fun Int?.string(vararg args: Any) = this?.string(args)

@Deprecated(Migration)
@Composable
fun Int.painter() = painterResource(this)

@Deprecated(Migration)
@Composable
fun Int?.painter() = this?.painter()

@Deprecated(Migration)
@Composable
fun Int.integerRes() = integerResource(this)

@Deprecated(Migration)
@Composable
fun Int?.integerRes() = this?.integerRes()

@Deprecated(Migration)
@Composable
fun Int.integerArrayres() = integerArrayResource(this)

@Deprecated(Migration)
@Composable
fun Int?.integerArrayres() = this?.integerArrayres()

@Deprecated(Migration)
@Composable
fun Int.booleanRes() = booleanResource(this)

@Deprecated(Migration)
@Composable
fun Int?.booleanRes() = this?.booleanRes()

@Deprecated(Migration)
@Composable
fun Int.color() = colorResource(this)

@Deprecated(Migration)
@Composable
fun Int?.color() = this?.color()

@Deprecated(Migration)
@Composable
fun Int.dimen() = dimensionResource(this)

@Deprecated(Migration)
@Composable
fun Int?.dimen() = this?.dimen()

/**
 * Produces an annotated string identical to the original string
 * For those times when an api requires AnnotatedString but you don't want to build one
 */
@Deprecated(Migration)
fun String.annotate() = AnnotatedString(this)

@Deprecated(Migration)
@Suppress("ComposableEventParameterNaming")
fun String.annotate(builder: AnnotatedString.Builder.(String) -> Unit) =
    buildAnnotatedString { builder(this@annotate) }

@Deprecated(Migration)
val displayDensity: Int @Composable get() = LocalConfiguration.current.densityDpi

@Composable
@Deprecated(Migration)
inline fun <T> withDensity(block: @Composable Density.() -> T) = with(LocalDensity.current) { block() }

@Deprecated(Migration)
val screenWidth: Dp @Composable get() = LocalConfiguration.current.screenWidthDp.dp

@Deprecated(Migration)
val screenHeight: Dp @Composable get() = LocalConfiguration.current.screenHeightDp.dp

@Deprecated(Migration)
val screenWidthPx: Float @Composable get() = withDensity { screenWidth.toPx() }

@Deprecated(Migration)
val screenHeightPx: Float @Composable get() = withDensity { screenHeight.toPx() }

@Deprecated(Migration)
@Composable
@Suppress("ComposableParametersOrdering")
inline fun <reified BoundService : Service, reified BoundServiceBinder : Binder> rememberBoundLocalService(
    flags: Int = Context.BIND_AUTO_CREATE,
    noinline getService: @DisallowComposableCalls BoundServiceBinder.() -> BoundService,
): State<BoundService?> {
    val context: Context = LocalContext.current
    val boundService = remember(context) { mutableStateOf<BoundService?>(null) }

    val serviceConnection: ServiceConnection = remember(context, getService) {
        object : ServiceConnection {
            override fun onServiceConnected(className: ComponentName, service: IBinder) {
                boundService.value = (service as BoundServiceBinder).getService()
            }

            override fun onServiceDisconnected(arg0: ComponentName) {
                boundService.value = null
            }
        }
    }
    DisposableEffect(context, serviceConnection, flags) {
        context.bindService(Intent(context, BoundService::class.java), serviceConnection, flags)

        onDispose { context.unbindService(serviceConnection) }
    }
    return boundService
}

@Deprecated(Migration)
@Composable
fun Text.string(): String = when (this) {
    is Dynamic -> text
    is Resource -> string(LocalContext.current)
}
