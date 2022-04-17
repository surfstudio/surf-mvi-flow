package ru.surfstudio.mvi.vm.compose

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.event.CommandEvent
import ru.surfstudio.mvi.flow.FlowEventHub

interface CommandEventObserver<E : Event, C : CommandEvent> {

    val hub: FlowEventHub<E>

    fun emitInScope(commandEvent: C) {
        //nothing
    }

    fun observeCommandEvents(): Flow<C> {
        return hub.observe().mapNotNull { it as? C }
    }
}