import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.reducer.Reducer
import ru.surfstudio.mvi.flow.DslFlowMiddleware
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.lifecycle.MVIView
import ru.surfstudio.mvi.lifecycle.MviViewModel

class TestView(
    private val scope: CoroutineScope,
) : MVIView<TestState, TestEvent> {

    private val testMiddleware = TestMiddleware()

    val eventsMiddlewareCount: Int
        get() = testMiddleware.eventsCount

    override val viewModel: MviViewModel<TestState, TestEvent> =
        TestViewModel(scope, testMiddleware)

    override val uiScope: CoroutineScope
        get() = scope
}

class TestViewModel(scope: CoroutineScope, middleware: TestMiddleware) :
    MviViewModel<TestState, TestEvent>() {

    override val state: FlowState<TestState> = FlowState(TestState())
    override val hub: FlowEventHub<TestEvent> = FlowEventHub()
    override val reducer: Reducer<TestEvent, TestState> = TestReducer()
    override val middleware: DslFlowMiddleware<TestEvent> = middleware

    init {
        viewModelScope.bind(hub, middleware, state, reducer)
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
                TestEvent::class react { eventsCount += 1 }
            )
        }
}