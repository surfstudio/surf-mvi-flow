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

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.reducer.Reducer
import ru.surfstudio.mvi.flow.DslFlowMiddleware
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.lifecycle.MviAndroidView
import ru.surfstudio.mvi.lifecycle.MviViewModel

abstract class BaseFlowTest {

    var testView: TestView? = null

    lateinit var middleware: TestMiddleware

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    @Before
    @Throws(Exception::class)
    fun setUp() {
        middleware = TestMiddleware()
        testView = TestView(middleware)
    }

    @ExperimentalCoroutinesApi
    @After
    fun destroy() {
        testView = null
    }
}

sealed class TestEvent : Event {
    object Logic : TestEvent()
    object Ui : TestEvent()
    data class Data(val value: String) : TestEvent()
}

data class TestState(
    val state: String = ""
)

class TestReducer : Reducer<TestEvent, TestState> {

    override fun reduce(state: TestState, event: TestEvent): TestState {
        return when (event) {
            is TestEvent.Data -> state.copy(state = event.value)
            else -> state
        }
    }
}


class TestMiddleware : DslFlowMiddleware<TestEvent> {

    var eventsCount = 0

    override fun transform(eventStream: Flow<TestEvent>): Flow<TestEvent> =
        eventStream.transformations {
            addAll(
                TestEvent::class react { eventsCount++ }
            )
        }
}

class TestViewModel(middleware: TestMiddleware) : MviViewModel<TestState, TestEvent>() {

    override val state: FlowState<TestState> = FlowState(TestState())
    override val hub: FlowEventHub<TestEvent> = FlowEventHub()
    override val reducer: Reducer<TestEvent, TestState> = TestReducer()
    override val middleware: DslFlowMiddleware<TestEvent> = middleware

    init {
        bindFlow()
    }
}

class TestView(middleware: TestMiddleware) : MviAndroidView<TestState, TestEvent> {

    override val viewModel: MviViewModel<TestState, TestEvent> = TestViewModel(middleware)

    // must be mocked for test based on mocked TestScope
    override val uiScope: CoroutineScope = viewModel.viewModelScope

    override fun getLifecycle(): Lifecycle =
        LifecycleRegistry(Mockito.mock(LifecycleOwner::class.java))
}
