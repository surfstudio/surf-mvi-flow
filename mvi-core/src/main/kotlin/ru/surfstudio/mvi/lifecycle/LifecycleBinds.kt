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
package ru.surfstudio.mvi.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.vm.MviViewModel
import ru.surfstudio.mvi.vm.android.MviView

/**
 * Sends lifecycle events to the shared event bus
 */
fun <E : Event> MviView<E>.bindsLifecycleEvent() {
    val mapToLifecycleEvent = viewModel.getMapperLifecycleEvent() ?: return
    lifecycle.addObserver(
        LifecycleEventObserver { _, event ->
            emit(mapToLifecycleEvent(event))
        }
    )
}

/**
 * Getting the function of converting a lifecycle event into a screen event
 */
private fun <E : Event> MviViewModel<E>.getMapperLifecycleEvent(): ((Lifecycle.Event) -> E)? {
    return (middleware as? MapperLifecycleEvent<E>)?.let { it::mapToLifecycleScreenEvent }
}