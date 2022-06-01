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
package ru.surfstudio.mvi.mappers.test

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.mappers.MapperFlowMiddleware
import ru.surfstudio.mvi.mappers.Request
import ru.surfstudio.mvi.mappers.RequestEvent
import ru.surfstudio.mvi.mappers.test.SampleEvent.LoadDataRequest

@OptIn(ExperimentalCoroutinesApi::class)
class MapperFlowMiddlewareTest {

    @Test
    fun testRequestEventSucceedTransformation() = runTest {
        SampleMiddleware().transform(
            flowOf(SampleEvent.LoadData(testError = false))
        ).test {
            // start with loading
            val loadingRequest = awaitItem() as LoadDataRequest
            println(loadingRequest)
            assert(loadingRequest.isLoading)
            // finish with data
            val dataRequest = awaitItem() as LoadDataRequest
            println(dataRequest)
            assert(dataRequest.isSuccess && dataRequest.request.getData() == SAMPLE_DATA)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testRequestEventErrorTransformation() = runTest {
        SampleMiddleware().transform(
            flowOf(SampleEvent.LoadData(testError = true))
        ).test {
            // start with loading
            val loadingRequest = awaitItem() as LoadDataRequest
            println(loadingRequest)
            assert(loadingRequest.isLoading)
            // finish with error
            val errorRequest = awaitItem() as LoadDataRequest
            println(errorRequest)
            assert(errorRequest.isError && errorRequest.request.getError() is NoSuchElementException)
            cancelAndIgnoreRemainingEvents()
        }
    }
}

private const val SAMPLE_DATA = "data"

private class SampleMiddleware : MapperFlowMiddleware<SampleEvent> {

    override fun transform(eventStream: Flow<SampleEvent>): Flow<SampleEvent> {
        return eventStream.transformations {
            addAll(
                SampleEvent.LoadData::class eventToStream { loadData(it.testError) }
            )
        }
    }

    private fun loadData(testError: Boolean): Flow<SampleEvent> {
        return flow {
            if (testError) {
                throw NoSuchElementException()
            } else {
                emit(SAMPLE_DATA)
            }
        }.asRequestEvent(::LoadDataRequest)
    }
}

private sealed class SampleEvent : Event {
    data class LoadData(val testError: Boolean) : SampleEvent()
    data class LoadDataRequest(
        override val request: Request<String>
    ) : RequestEvent<String>, SampleEvent()
}