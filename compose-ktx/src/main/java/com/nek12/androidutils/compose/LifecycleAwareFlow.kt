package com.nek12.androidutils.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

// credits: https://proandroiddev.com/how-to-collect-flows-lifecycle-aware-in-jetpack-compose-babd53582d0b

@Composable
fun <T> rememberLifecycleFlow(
    flow: Flow<T>,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
): Flow<T> = remember(flow, lifecycleOwner, lifecycleState) {
    flow.flowWithLifecycle(lifecycleOwner.lifecycle, lifecycleState)
}

@Composable
@Deprecated("Use collectAsStateWithLifecycle from androix instead")
fun <T : R, R> Flow<T>.collectAsStateOnLifecycle(
    initial: R,
    context: CoroutineContext = EmptyCoroutineContext,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
): State<R> {
    val lifecycleAwareFlow = rememberLifecycleFlow(this, lifecycleState)
    return lifecycleAwareFlow.collectAsState(initial, context)
}

@Suppress("StateFlowValueCalledInComposition")
@Composable
@Deprecated("Use collectAsStateWithLifecycle from androix instead")
fun <T> StateFlow<T>.collectAsStateOnLifecycle(
    context: CoroutineContext = EmptyCoroutineContext,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
): State<T> = collectAsStateOnLifecycle(value, context, lifecycleState)

@Composable
@Suppress("ComposableNaming", "ComposableParametersOrdering")
fun <T> Flow<T>.collectOnLifecycle(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    onNext: suspend CoroutineScope.(T) -> Unit,
) {
    val lifecycleFlow = rememberLifecycleFlow(this, lifecycleState)
    LaunchedEffect(lifecycleFlow) {
        lifecycleFlow.collect { onNext(it) }
    }
}
