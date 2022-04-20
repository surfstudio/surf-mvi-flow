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
package ru.surfstudio.mvi.flow;

import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.event.LifecycleMviEvent

/**
 * Middleware, that reacts on android lifecycle.
 *
 * To receive events, you need to add event that implements [LifecycleMviEvent]
 * and for your viewModel implements [MapperLifecycleEvent]
 * The last step is to call the [bindLifecycleEvent]
 * for the entity implementing the MviView interface
 */
interface LifecycleMiddleware<T : Event> : DslFlowMiddleware<T> {
    fun Flow<T>.onCreate() = filterLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun Flow<T>.onStart() = filterLifecycleEvent(Lifecycle.Event.ON_START)
    fun Flow<T>.onStop() = filterLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun Flow<T>.onResume() = filterLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun Flow<T>.onDestroy() = filterLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun Flow<T>.onPause() = filterLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun Flow<T>.onAny() = filterLifecycleEvent(Lifecycle.Event.ON_ANY)

    fun EventTransformerList<T>.onCreate() = eventStream.onCreate()
    fun EventTransformerList<T>.onStart() = eventStream.onStart()
    fun EventTransformerList<T>.onResume() = eventStream.onResume()
    fun EventTransformerList<T>.onPause() = eventStream.onPause()
    fun EventTransformerList<T>.onStop() = eventStream.onStop()
    fun EventTransformerList<T>.onDestroy() = eventStream.onDestroy()
    fun EventTransformerList<T>.onAny() = eventStream.onAny()
}

private fun <T> Flow<T>.filterLifecycleEvent(lifecycleEvent: Lifecycle.Event) = this
    .filter { it is LifecycleMviEvent && it.event == lifecycleEvent }
    .map { it }
