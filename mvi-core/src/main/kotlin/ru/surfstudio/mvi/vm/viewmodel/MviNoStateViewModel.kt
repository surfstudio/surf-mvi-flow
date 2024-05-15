package ru.surfstudio.mvi.vm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.excisable.StatefulMviStore
import ru.surfstudio.mvi.excisable.StatelessMviStore
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.logging.AndroidLogger
import ru.surfstudio.mvibase.logging.MviLogger

abstract class MviNoStateViewModel<E : Event> : ViewModel(), StatelessMviStore<E> {
    override val mviFlowScope: CoroutineScope = viewModelScope
    override val hub: FlowEventHub<E> = FlowEventHub()
    override val logger: MviLogger = AndroidLogger
}

abstract class MviStatefulViewModel<S : Any, E : Event> : MviNoStateViewModel<E>(),
    StatefulMviStore<S, E>