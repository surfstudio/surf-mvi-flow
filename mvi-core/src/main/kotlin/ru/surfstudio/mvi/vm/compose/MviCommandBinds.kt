package ru.surfstudio.mvi.vm.compose

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.surfstudio.mvi.core.event.CommandEvent
import ru.surfstudio.mvi.core.event.Event

/** Syntactic sugar fun for easy linking in @Composable command events with MVI */
@SuppressLint("ComposableNaming")
@Composable
fun <C : CommandEvent, E : Event> CommandObserver<E, C>.bindsCommandEvent(
    onCommandEventListener: CoroutineScope.(C) -> Unit
) {
    val scope = rememberCoroutineScope()
    bindsCommandEvent(scope = scope, onCommandEventListener = onCommandEventListener)
}

/** Syntactic sugar fun for easy linking in @Composable command events with MVI */
@SuppressLint("ComposableNaming", "CoroutineCreationDuringComposition")
@Composable
fun <C : CommandEvent, E : Event> CommandObserver<E, C>.bindsCommandEvent(
    scope: CoroutineScope,
    onCommandEventListener: CoroutineScope.(C) -> Unit
) {
    scope.launch {
        observeCommandEvents().onEach {
            this.onCommandEventListener(it)
        }.collect()
    }
}