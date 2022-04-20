package ru.surfstudio.mvi.vm.android

import androidx.lifecycle.Lifecycle
import ru.surfstudio.mvi.core.event.Event

/**
 * Map [Lifecycle.Event] from lifecycleScope to [LifecycleMviEvent]
 */
interface MapperLifecycleEvent<E : Event> {
    fun mapToLifecycleScreenEvent(event: Lifecycle.Event): E
}