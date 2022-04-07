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

import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class LogicTest : BaseFlowTest() {

    @Test
    fun testEventsPassingMiddleware() {
        Assert.assertEquals(0, middleware.eventsCount)

        testView?.emit(TestEvent.Ui)
        testView?.emit(TestEvent.Logic)
        testView?.emit(TestEvent.Data(""))

        Assert.assertEquals(3, middleware.eventsCount)
    }
}
