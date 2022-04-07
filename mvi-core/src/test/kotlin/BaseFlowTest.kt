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

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseFlowTest {

    protected var testView: TestView? = null
    protected var testMiddleware: TestMiddleware? = null
    protected var testReducer: TestReducer? = null
    protected var testViewModel: TestViewModel? = null

    @get:Rule
    val coroutineRule = TestCoroutineDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    protected fun initEntities(scope: TestScope) {
        testMiddleware = TestMiddleware()
        testReducer = TestReducer()
        testViewModel =
            TestViewModel(testMiddleware!!, testReducer!!, scope)
        testView = TestView(testViewModel!!, scope)
    }
}
