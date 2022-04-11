package ru.surfstudio.mvi.vm.compose

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.event.SingleLiveEvent

interface SingleLiveEventEmmiter<E : SingleLiveEvent> {

    fun observeFlowEvents(): Flow<Event>

    fun observeSingleLiveEvents(): Flow<E> {
        return observeFlowEvents().mapNotNull { it as? E }
    }
}