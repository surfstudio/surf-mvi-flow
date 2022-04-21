package ru.surfstudio.mvi.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.vm.MviViewModel
import ru.surfstudio.mvi.vm.android.MviView

/**
 * Sends lifecycle events to the shared event bus
 */
fun <E : Event> MviView<E>.bindsLifecycleEvent() {
    val mapToLifecycleEvent = viewModel.getMapperLifecycleEvent() ?: return
    lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            emit(mapToLifecycleEvent(event))
        }
    })
}

/**
 * Getting the function of converting a lifecycle event into a screen event
 */
private fun <E : Event> MviViewModel<E>.getMapperLifecycleEvent(): ((Lifecycle.Event) -> E)? {
    return (this as? MapperLifecycleEvent<E>)?.let { it::mapToLifecycleScreenEvent }
}