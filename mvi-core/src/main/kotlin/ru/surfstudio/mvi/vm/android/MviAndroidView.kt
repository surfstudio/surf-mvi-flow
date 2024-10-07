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
package ru.surfstudio.mvi.vm.android

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.lifecycle.MapperLifecycleEvent
import ru.surfstudio.mvi.vm.MviStatefulViewModel
import ru.surfstudio.mvi.vm.MviViewModel

interface MviView<E : Event> : LifecycleOwner {

    /**
     * Scope to observe on state changes and to emit events
     */
    val uiScope: CoroutineScope
        get() = lifecycleScope

    /**
     * viewModel providing event hub for event emission
     */
    val viewModel: MviViewModel<E>

    /**
     * Sends lifecycle events to the shared event bus
     */
    fun bindsLifecycleEvent() {
        val mapToLifecycleEvent = getMapperLifecycleEvent() ?: return
        lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                emit(mapToLifecycleEvent(event))
            }
        )
    }

    /**
     * Emits [event] to a hub which is provided by [viewModel]
     */
    fun emit(event: E) {
        uiScope.launch {
            viewModel.hub.emit(event)
        }
    }

    /**
     * Getting the function of converting a lifecycle event into a screen event
     */
    private fun getMapperLifecycleEvent(): ((Lifecycle.Event) -> E)? {
        return (viewModel.middleware as? MapperLifecycleEvent<E>)?.let { it::mapToLifecycleScreenEvent }
    }
}

interface MviStatefulView<S : Any, E : Event> : MviView<E> {

    /**
     * viewModel providing event hub for event emission and observable state
     */
    override val viewModel: MviStatefulViewModel<S, E>

    /**
     * Subscribes to state updates with the [collector]
     */
    fun observeState(collector: suspend (S) -> Unit) {
        uiScope.launch(Dispatchers.Main) {
            viewModel.stateHolder
                .observeState()
                .collect { collector(it) }
        }
    }
}