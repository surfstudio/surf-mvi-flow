/*
 * Copyright 2022 Surf LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.surfstudio.mvi.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.reducer.Reducer
import ru.surfstudio.mvi.flow.DslFlowMiddleware
import ru.surfstudio.mvi.flow.FlowBinder
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowState

/**
 * An interface of ViewModel providing implementations of observable
 * hub of events based on Coroutines Flow
 */
abstract class MviViewModelWithoutState<E : Event> : ViewModel(), FlowBinder {

    abstract val middleware: DslFlowMiddleware<E>
    open val hub: FlowEventHub<E> = FlowEventHub()

    /** Must be called in descendant class `init` */
    open fun bindFlow() {
        viewModelScope.bind(hub, middleware)
    }
}

/**
 * An interface of ViewModel providing implementations of observable
 * state and hub of events based on Coroutines Flow
 */
abstract class MviViewModel<S : Any, E : Event> : MviViewModelWithoutState<E>() {

    abstract val state: FlowState<S>
    abstract val reducer: Reducer<E, S>

    override fun bindFlow() {
        viewModelScope.bind(hub, middleware, state, reducer)
    }
}