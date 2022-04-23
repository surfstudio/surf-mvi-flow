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

import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import ru.surfstudio.mvi.flow.BaseFlowMiddleware
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.flow.app.simple.request.RequestState
import ru.surfstudio.mvi.flow.app.simple.SimpleEvent.*
import java.io.IOException

class SimpleMiddleware(
    private val state: FlowState<SimpleState>
) : BaseFlowMiddleware<SimpleEvent> {

    override fun transform(eventStream: Flow<SimpleEvent>): Flow<SimpleEvent> {
        return eventStream.transformations {
            addAll(
                onCreate() eventToEvent { SimpleClick },
                StartLoadingClick::class
                    filter { state.currentState.request == RequestState.None }
                    streamToStream { requestFlow(it) },
                SimpleClick::class react {
                    println("debug react sample")
                },
                SimpleClick::class streamToStream { clicks -> clicksFlow(clicks) },
            )
        }
    }

    override fun mapToLifecycleScreenEvent(event: Lifecycle.Event) = LifecycleEvent(event)

    @OptIn(FlowPreview::class)
    private fun clicksFlow(clicks: Flow<SimpleClick>): Flow<TitleUpdate> {
        return clicks.debounce(1000)
            .map { TitleUpdate("Ты перестал кликать") }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun requestFlow(flow: Flow<StartLoadingClick>): Flow<RequestEvent> {
        return flow.flatMapLatest {
            flow {
                delay(3000)
                if (System.currentTimeMillis() % 2 == 0L) {
                    throw IOException()
                }
                emit(RequestEvent(RequestState.Success))
            }.onStart {
                emit(RequestEvent(RequestState.Loading))
            }.catch {
                emit(RequestEvent(RequestState.Error))
            }.onCompletion {
                delay(2000)
                emit(RequestEvent(RequestState.None))
            }
        }
    }

}