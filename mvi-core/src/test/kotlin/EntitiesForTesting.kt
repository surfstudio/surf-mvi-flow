import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.TestScope
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.hub.EventHub
import ru.surfstudio.mvi.core.reducer.Reducer
import ru.surfstudio.mvi.flow.DslFlowMiddleware
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.lifecycle.MVIView
import ru.surfstudio.mvi.lifecycle.MviViewModel
import java.lang.NullPointerException
import kotlin.coroutines.CoroutineContext

class TestView(
    private val scope: CoroutineScope,
    override val viewModel: MviViewModel<TestState, TestEvent>
) : MVIView<TestState, TestEvent> {

    override val uiScope: CoroutineScope
        get() = scope
}

class TestViewModel(
    private val testMiddleware: TestMiddleware,
    private val testReducer: TestReducer,
    private val testScope: CoroutineScope
) : MviViewModel<TestState, TestEvent>() {

    override val state: FlowState<TestState> = FlowState(TestState())
    override val hub: FlowEventHub<TestEvent> = FlowEventHub()
    override val reducer: Reducer<TestEvent, TestState> = testReducer
    override val middleware: DslFlowMiddleware<TestEvent> = testMiddleware

    init {
        testScope.bind(hub, middleware, state, reducer)
    }
}

sealed class TestEvent : Event {
    object Logic : TestEvent()
    object Ui : TestEvent()
    data class Data(val value: String) : TestEvent()
}

data class TestState(val state: String = "")

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