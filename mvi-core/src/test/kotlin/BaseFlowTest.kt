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
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BaseFlowTest {

    protected var testView: TestView? = null
    protected var testMiddleware: TestMiddleware? = null
    protected var testReducer: TestReducer? = null
    protected var testViewModel: TestViewModel? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val testDispatcher = StandardTestDispatcher()
    @OptIn(ExperimentalCoroutinesApi::class)
    val testScope = TestScope(testDispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun onStart() {
        Dispatchers.setMain(StandardTestDispatcher(testScope.testScheduler))
    }


    @ExperimentalCoroutinesApi
    @After
    fun destroy() {
        Dispatchers.resetMain()

        testMiddleware = null
        testViewModel = null
        testView = null
    }
}
