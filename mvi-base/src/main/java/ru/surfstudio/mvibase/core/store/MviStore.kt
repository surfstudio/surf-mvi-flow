package ru.surfstudio.mvi.excisable

import kotlinx.coroutines.CoroutineScope
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.reducer.Reducer
import ru.surfstudio.mvi.flow.DslFlowMiddleware
import ru.surfstudio.mvi.flow.FlowBinder
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvibase.logging.MviLogger

interface StatelessMviStore<E : Event> : FlowBinder {
    val middleware: DslFlowMiddleware<E>
    val hub: FlowEventHub<E>
    val mviFlowScope: CoroutineScope
    val logger: MviLogger

    /** Must be called in descendant class `init` */
    fun bindFlow() {
        mviFlowScope.bind(hub, middleware, logger)
    }
}

interface StatefulMviStore<S : Any, E : Event> : StatelessMviStore<E> {
    val state: FlowState<S>
    val reducer: Reducer<E, S>

    /** Must be called in descendant class `init` */
    override fun bindFlow() {
        mviFlowScope.bind(hub, middleware, state, reducer, logger)
    }
}