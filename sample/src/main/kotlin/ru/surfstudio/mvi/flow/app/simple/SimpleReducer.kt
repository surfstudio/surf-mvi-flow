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

import ru.surfstudio.mvi.core.reducer.Reducer
import ru.surfstudio.mvi.flow.app.simple.request.RequestState

data class SimpleState(
    val title: String = "Кликай меня полностью",
    val counter: Int = 42,
    val request: RequestState = RequestState.None
)

class SimpleReducer: Reducer<SimpleEvent, SimpleState> {

    override fun reduce(state: SimpleState, event: SimpleEvent): SimpleState {
        return when(event) {
            is SimpleEvent.SimpleClick -> state.copy(title = "Клик")
            is SimpleEvent.TitleUpdate -> state.copy(title = event.title)
            is SimpleEvent.IncrementClick -> state.copy(counter = state.counter + 1)
            is SimpleEvent.DecrementClick -> state.copy(counter = state.counter - 1)
            is SimpleEvent.RequestEvent -> state.copy(request = event.request)
            else -> state
        }
    }
}