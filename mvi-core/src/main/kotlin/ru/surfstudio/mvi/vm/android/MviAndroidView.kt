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

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import ru.surfstudio.mvi.core.event.Event
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
     * Emits [event] to a hub which is provided by [viewModel]
     */
    fun emit(event: E) {
        uiScope.launch {
            viewModel.hub.emit(event)
        }
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
    @OptIn(InternalCoroutinesApi::class)
    fun observeState(collector: suspend (S) -> Unit) {
        uiScope.launch(Dispatchers.Main) {
            viewModel.state
                .observeState()
                .collect { collector(it) }
        }
    }
}