package ru.surfstudio.mvi.excisable

import kotlinx.coroutines.CoroutineScope
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.reducer.Reducer
import ru.surfstudio.mvi.flow.BaseFlowMiddleware
import ru.surfstudio.mvi.flow.DslFlowMiddleware
import ru.surfstudio.mvi.flow.FlowBinder
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowState

interface TeaLikeStore<S : Any, E : Event> : FlowBinder {
    val middleware: DslFlowMiddleware<E>
    val hub: FlowEventHub<E>
    val state: FlowState<S>
    val reducer: Reducer<E, S>
    val mviFlowScope: CoroutineScope

    fun bindFlow()
}

interface StoreHolder<S : Any, E : Event> {
    val store: TeaLikeStore<S, E>

    // Такой вариант
    val middleware: DslFlowMiddleware<E>
        get() = store.middleware
}

class Store<TState : Any, TEvent : Event>(
    initialState: TState,
    reducer: Reducer<TEvent, TState>,
    flowMiddleware: DslFlowMiddleware<TEvent>,
    coroutineScope: CoroutineScope,
    eventHub: FlowEventHub<TEvent>
): TeaLikeStore<TState, TEvent> {
    override val middleware: DslFlowMiddleware<TEvent> = flowMiddleware
    override val hub: FlowEventHub<TEvent> = eventHub
    override val state: FlowState<TState> = FlowState(initialState)
    override val reducer: Reducer<TEvent, TState> = reducer

    override val mviFlowScope: CoroutineScope = coroutineScope

    override fun bindFlow() {
        mviFlowScope.bind(hub, middleware, state, reducer)
    }
}

fun <TState : Any, TEvent : Event> StandardStore(
    initialState: TState,
    reducer: Reducer<TEvent, TState>,
    flowMiddleware: DslFlowMiddleware<TEvent>,
    coroutineScope: CoroutineScope,
    eventHub: FlowEventHub<TEvent> = FlowEventHub()
): Store<TState, TEvent> {
    return Store(
        initialState = initialState,
        reducer = reducer,
        flowMiddleware = flowMiddleware,
        coroutineScope = coroutineScope,
        eventHub = eventHub
    )
}

