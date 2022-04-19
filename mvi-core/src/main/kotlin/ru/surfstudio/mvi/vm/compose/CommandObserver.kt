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
package ru.surfstudio.mvi.vm.compose

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.event.CommandEvent
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.vm.MviViewModel

/**
 * Interface for ViewModel that implements monitoring CommandEvent that arrive at the hub
 */
interface CommandObserver<E : Event, C : CommandEvent> {

    val hub: FlowEventHub<E>

    fun observeCommandEvents(): Flow<C> {
        return hub.observe().mapNotNull { it as? C }
    }
}

/**
 * Emit commandEvent in hub
 */
fun <E> MviViewModel<E>.emitInScope(commandEvent: E) where E : Event, E : CommandEvent {
    viewModelScope.launch {
        hub.emit(commandEvent)
    }
}