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

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.flow.app.simple.request.RequestState
import ru.surfstudio.mvi.flow.DslFlowMiddleware
import ru.surfstudio.mvi.flow.LifecycleMiddleware
import java.io.IOException

class SimpleMiddleware(
    private val state: FlowState<SimpleState>
) : DslFlowMiddleware<SimpleEvent>, LifecycleMiddleware<SimpleEvent> {

    override fun transform(eventStream: Flow<SimpleEvent>): Flow<SimpleEvent> {
        return eventStream.transformations {
            addAll(
                onCreate() eventToEvent { SimpleEvent.SimpleClick },
                SimpleEvent.StartLoadingClick::class
                    filter { state.currentState.request == RequestState.None }
                    streamToStream { requestFlow(it) },
                SimpleEvent.SimpleClick::class react {
                    println("debug react sample")
                },
                SimpleEvent.SimpleClick::class streamToStream { clicks -> clicksFlow(clicks) },
            )
        }
    }

    @OptIn(FlowPreview::class)
    private fun clicksFlow(clicks: Flow<SimpleEvent.SimpleClick>): Flow<SimpleEvent.TitleUpdate> {
        return clicks.debounce(1000)
            .map { SimpleEvent.TitleUpdate("Ты перестал кликать") }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun requestFlow(flow: Flow<SimpleEvent.StartLoadingClick>): Flow<SimpleEvent.RequestEvent> {
        return flow.flatMapLatest {
            flow {
                delay(3000)
                if (System.currentTimeMillis() % 2 == 0L) {
                    throw IOException()
                }
                emit(SimpleEvent.RequestEvent(RequestState.Success))
            }.onStart {
                emit(SimpleEvent.RequestEvent(RequestState.Loading))
            }.catch {
                emit(SimpleEvent.RequestEvent(RequestState.Error))
            }.onCompletion {
                delay(2000)
                emit(SimpleEvent.RequestEvent(RequestState.None))
            }
        }
    }
}