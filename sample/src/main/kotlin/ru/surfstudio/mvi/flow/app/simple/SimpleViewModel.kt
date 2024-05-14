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
package ru.surfstudio.mvi.flow.app.simple

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.reducer.Reducer
import ru.surfstudio.mvi.excisable.NewMviStatefulViewModel
import ru.surfstudio.mvi.excisable.StandardStore
import ru.surfstudio.mvi.excisable.Store
import ru.surfstudio.mvi.excisable.StoreHolder
import ru.surfstudio.mvi.excisable.TeaLikeStore
import ru.surfstudio.mvi.flow.DslFlowMiddleware
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.vm.MviStatefulViewModel

class SimpleViewModel : MviStatefulViewModel<SimpleState, SimpleEvent>() {

    override val state: FlowState<SimpleState> = FlowState(SimpleState())
    override val middleware: SimpleMiddleware = SimpleMiddleware(state)
    override val reducer: SimpleReducer = SimpleReducer()

    init {
        bindFlow()
    }

}

// MviStoreImpl
class NewSimpleViewModel : NewMviStatefulViewModel<SimpleState, SimpleEvent>() {

    override val state: FlowState<SimpleState> = FlowState(SimpleState())
    override val middleware: SimpleMiddleware = SimpleMiddleware(state)
    override val reducer: SimpleReducer = SimpleReducer()

    init {
        bindFlow()
    }
}

//TeaLikeStore
class TeaLikeSimpleViewModel : ViewModel(), StoreHolder<SimpleState, SimpleEvent> {
    override val store = SimpleStore(coroutineScope = viewModelScope)

    init {
        store.bindFlow()
    }
}

fun SimpleStore(
    coroutineScope: CoroutineScope,
    initialState: SimpleState = SimpleState(),
    reducer: Reducer<SimpleEvent, SimpleState> = SimpleReducer(),
    eventHub: FlowEventHub<SimpleEvent> = FlowEventHub()
): Store<SimpleState, SimpleEvent> {
    val state = FlowState(initialState)
    return Store(
        initialState = state.currentState,
        reducer = reducer,
        flowMiddleware = SimpleMiddleware(state),
        coroutineScope = coroutineScope,
        eventHub = eventHub
    )
}