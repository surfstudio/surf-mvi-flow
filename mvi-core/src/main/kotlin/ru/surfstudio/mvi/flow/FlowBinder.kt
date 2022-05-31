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
package ru.surfstudio.mvi.flow

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.middleware.Middleware
import ru.surfstudio.mvi.core.reducer.Reactor

/**
 * An object that binds together everything in coroutineScope and logs events
 */
interface FlowBinder {

    fun <T : Event, SH> CoroutineScope.bind(
        eventHub: FlowEventHub<T>,
        middleware: Middleware<Flow<T>, Flow<T>>,
        stateHolder: SH,
        reactor: Reactor<T, SH>
    ) {
        val eventFlow = eventHub.observe()
            .onEach { event: T ->
                Log.d(TAG, event.toString())
                reactor.react(stateHolder, event)
            }.catch {
                Log.e(TAG, it.message, it)
                throw it
            }.shareIn(this, SharingStarted.Eagerly)
        this@bind.launch {
            middleware.transform(eventFlow)
                .collect { transformedEvent: T ->
                    eventHub.emit(transformedEvent)
                }
        }
    }

    companion object {
        const val TAG = "FlowBinder"
    }
}