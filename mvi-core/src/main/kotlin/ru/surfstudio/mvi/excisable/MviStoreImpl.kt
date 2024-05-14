package ru.surfstudio.mvi.excisable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.reducer.Reducer
import ru.surfstudio.mvi.flow.DslFlowMiddleware
import ru.surfstudio.mvi.flow.FlowBinder
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowState

//TODO-1 Определиться с именованием
//TODO-2 Определиться с пакетом и вынести в котлин модуль (изучить возможность переноса в shared)

/*
Проблемы:
    - Timber зависит от Android -> Завязаться на абстрактный логгер с платформенной реализацией

 */

// Общая часть
interface StatelessMviStore<E : Event> : FlowBinder {
    val middleware: DslFlowMiddleware<E>
    val hub: FlowEventHub<E>
    val mviFlowScope: CoroutineScope

    /** Must be called in descendant class `init` */
    fun bindFlow() {
        mviFlowScope.bind(hub, middleware)
    }
}

interface StatefulMviStore<S : Any, E : Event> : StatelessMviStore<E> {
    val state: FlowState<S>
    val reducer: Reducer<E, S>

    /** Must be called in descendant class `init` */
    override fun bindFlow() {
        mviFlowScope.bind(hub, middleware, state, reducer)
    }
}

// Платформенная Android
abstract class NewMviViewModel<E : Event> : ViewModel(), StatelessMviStore<E> {
    override val mviFlowScope: CoroutineScope = viewModelScope
    override val hub: FlowEventHub<E> = FlowEventHub()

    /** Must be called in descendant class `init` */
    override fun bindFlow() {
        mviFlowScope.bind(hub, middleware)
    }
}

abstract class NewMviStatefulViewModel<S : Any, E : Event> : NewMviViewModel<E>(), StatefulMviStore<S, E> {
    /** Must be called in descendant class `init` */
    override fun bindFlow() {
        mviFlowScope.bind(hub, middleware, state, reducer)
    }
}