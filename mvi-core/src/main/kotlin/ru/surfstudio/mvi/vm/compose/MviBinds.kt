package ru.surfstudio.mvi.vm.compose

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.vm.MviViewModelWithoutState

/** Syntax sugar fun for convenient binding in @Composable with MVI */
@SuppressLint("ComposableNaming")
@Composable
infix fun <E : Event> MviViewModelWithoutState<E>.binds(
    render: @Composable ComposedViewContext<E>.() -> Unit
) {
    val scope = rememberCoroutineScope()

    ComposedViewContext<E> { event ->
        scope.launch {
            hub.emit(event)
        }
    }.render()
}