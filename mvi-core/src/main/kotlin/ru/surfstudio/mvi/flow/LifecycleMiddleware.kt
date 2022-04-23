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
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.event.MviLifecycleEvent

/**
 * Middleware, that reacts on android lifecycle.
 *
 * To receive events, you need to add event that implements [MviLifecycleEvent]
 * and for your viewModel implements [MapperLifecycleEvent]
 * The last step is to call the [bindLifecycleEvent]
 * for the entity implementing the MviView interface
 */
interface LifecycleMiddleware<E : Event> : DslFlowMiddleware<E> {
    fun Flow<E>.onCreate() = filterLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun Flow<E>.onStart() = filterLifecycleEvent(Lifecycle.Event.ON_START)
    fun Flow<E>.onStop() = filterLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun Flow<E>.onResume() = filterLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun Flow<E>.onDestroy() = filterLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun Flow<E>.onPause() = filterLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun Flow<E>.onAny() = filterLifecycleEvent(Lifecycle.Event.ON_ANY)

    fun EventTransformerList<E>.onCreate() = eventStream.onCreate()
    fun EventTransformerList<E>.onStart() = eventStream.onStart()
    fun EventTransformerList<E>.onResume() = eventStream.onResume()
    fun EventTransformerList<E>.onPause() = eventStream.onPause()
    fun EventTransformerList<E>.onStop() = eventStream.onStop()
    fun EventTransformerList<E>.onDestroy() = eventStream.onDestroy()
    fun EventTransformerList<E>.onAny() = eventStream.onAny()
}

private fun <T> Flow<T>.filterLifecycleEvent(lifecycleEvent: Lifecycle.Event) =
    filter { it is MviLifecycleEvent<*> && it.event == lifecycleEvent }
