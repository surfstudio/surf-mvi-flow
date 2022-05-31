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

import android.util.Log
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
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.mockStatic
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.reducer.Reducer
import ru.surfstudio.mvi.flow.DslFlowMiddleware
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.vm.MviStatefulViewModel
import ru.surfstudio.mvi.vm.android.MviStatefulView

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseFlowTest {

    var testView: TestView? = null

    lateinit var middleware: TestMiddleware
    lateinit var reducer: TestReducer

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private var mockedLog: MockedStatic<Log>? = null

    @Before
    fun setUp() {
        mockedLog = mockStatic(Log::class.java)
        middleware = TestMiddleware()
        reducer = TestReducer()
        testView = TestView(middleware, reducer)
    }

    @After
    fun destroy() {
        testView = null
        mockedLog?.close()
    }
}

sealed class TestEvent : Event {
    object Logic : TestEvent()
    object Ui : TestEvent()
    data class Data(val value: String) : TestEvent()
}

const val INITIAL_STATE_VALUE = ""

data class TestState(
    val state: String = INITIAL_STATE_VALUE
)

class TestReducer : Reducer<TestEvent, TestState> {
    var eventsCount = 0

    override fun reduce(state: TestState, event: TestEvent): TestState {
        eventsCount++
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

class TestViewModel(middleware: TestMiddleware, reducer: TestReducer) :
    MviStatefulViewModel<TestState, TestEvent>() {

    override val state: FlowState<TestState> = FlowState(TestState())
    override val hub: FlowEventHub<TestEvent> = FlowEventHub()
    override val reducer: Reducer<TestEvent, TestState> = reducer
    override val middleware: DslFlowMiddleware<TestEvent> = middleware

    init {
        bindFlow()
    }
}

class TestView(middleware: TestMiddleware, reducer: TestReducer) :
    MviStatefulView<TestState, TestEvent> {

    override val viewModel: MviStatefulViewModel<TestState, TestEvent> =
        TestViewModel(middleware, reducer)

    // must be mocked for test based on mocked TestScope
    override val uiScope: CoroutineScope = viewModel.viewModelScope

    override fun getLifecycle(): Lifecycle =
        LifecycleRegistry(Mockito.mock(LifecycleOwner::class.java))
}
