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
package ru.surfstudio.mvi.flow.lifecycle

import androidx.lifecycle.ViewModel
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.hub.MutableHub
import ru.surfstudio.mvi.flow.*

/**
 * An interface of ViewModel providing implementations of observable
 * state and hub of events based on Coroutines Flow
 */
abstract class MviViewModel<S : Any, E : Event> : ViewModel() {

    abstract val stateHolder: ImmutableFlowStateHolder<S>
    abstract val hub: MutableHub<E>

}