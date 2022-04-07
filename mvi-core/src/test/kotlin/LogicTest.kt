/*
  Copyright (c) 2020, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LogicTest : BaseFlowTest() {

    @Test
    fun testEventsPassingMiddleware() = runTest {
        initEntities(this)

        Assert.assertEquals(0, testMiddleware?.eventsCount)

        testView?.emit(TestEvent.Logic)

        Assert.assertEquals(0, testMiddleware?.eventsCount)
    }

    @Test
    fun testEvents() = runTest {
        val flow = flow<Int> {
            emit(4)
        }

        var counter = 0
        flow.collect {
            counter = 4
        }

        Assert.assertEquals(4, counter)
    }
}

