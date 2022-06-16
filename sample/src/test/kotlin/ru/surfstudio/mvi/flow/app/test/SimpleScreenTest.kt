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
package ru.surfstudio.mvi.flow.app.test

import org.junit.Test
import ru.surfstudio.mvi.flow.app.simple.SimpleEvent
import ru.surfstudio.mvi.flow.app.simple.SimpleViewModel
import ru.surfstudio.mvi.flow.app.simple.request.RequestState
import ru.surfstudio.mvi.flow.app.test.core.BaseMviScreenTest
import ru.surfstudio.mvi.flow.app.test.core.BaseMviScreenTest.ViewModelProducer

class SimpleScreenTest : BaseMviScreenTest() {

    private val viewModelProducer = ViewModelProducer { SimpleViewModel() }

    @Test
    fun `test multiple SimpleClick emits map to a single TitleUpdate`() {
        val startEvent = SimpleEvent.SimpleClick
        viewModelProducer.produce().hub.middlewareTest(
            startEvents = listOf(startEvent, startEvent, startEvent),
            expectedFinalEventCheck = { it is SimpleEvent.TitleUpdate }
        )
    }

    @Test
    fun `test start loading click mappings`() {
        val startEvent = SimpleEvent.StartLoadingClick
        viewModelProducer.produce().hub.middlewareTest(
            startEvent = startEvent,
            expectedEventsChecks = listOf(
                { it == startEvent },
                { (it as SimpleEvent.RequestEvent).request == RequestState.Loading },
                // skip random error or success check
                { true },
                { (it as SimpleEvent.RequestEvent).request == RequestState.None }
            )
        )
    }
}