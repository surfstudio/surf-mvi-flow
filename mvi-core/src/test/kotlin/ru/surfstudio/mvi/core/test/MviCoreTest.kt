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
package ru.surfstudio.mvi.core.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MviCoreTest : BaseFlowTest() {

    @Test
    fun testEventsPassingMiddleware() = runTest {
        Assert.assertEquals(0, middleware.eventsCount)

        testView?.emit(TestEvent.Ui)
        testView?.emit(TestEvent.Logic)
        testView?.emit(TestEvent.Data(""))

        Assert.assertEquals(3, middleware.eventsCount)
    }

    @Test
    fun testEventsPassingReducer() = runTest {
        Assert.assertEquals(0, reducer.eventsCount)

        testView?.emit(TestEvent.Ui)
        testView?.emit(TestEvent.Logic)
        testView?.emit(TestEvent.Data(""))

        Assert.assertEquals(3, reducer.eventsCount)
    }

    @Test
    fun testStateChanges() = runTest {
        val flow = testView?.viewModel?.state?.observeState()

        Assert.assertEquals(flow?.firstOrNull()?.state, INITIAL_STATE_VALUE)
        testView?.emit(TestEvent.Data("test"))
        Assert.assertEquals(flow?.firstOrNull()?.state, "test")
    }

    @Test
    fun testStateUnchangedOnLogic() = runTest {
        val flow = testView?.viewModel?.state?.observeState()

        Assert.assertEquals(flow?.firstOrNull()?.state, INITIAL_STATE_VALUE)
        testView?.emit(TestEvent.Logic)
        Assert.assertEquals(flow?.firstOrNull()?.state, INITIAL_STATE_VALUE)
    }
}
