import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import org.mockito.Mockito
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.hub.EventHub
import ru.surfstudio.mvi.core.middleware.Middleware
import ru.surfstudio.mvi.core.reducer.Reactor
import ru.surfstudio.mvi.core.reducer.Reducer
import ru.surfstudio.mvi.flow.DslFlowMiddleware
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.lifecycle.MVIView
import ru.surfstudio.mvi.lifecycle.MviViewModel
import java.lang.NullPointerException
import kotlin.coroutines.CoroutineContext

class TestView(
    override val viewModel: TestMviViewModel<TestState, TestEvent>,
    private val scope: CoroutineScope
) : TestMVIView<TestState, TestEvent> {

    override val uiScope: CoroutineScope
        get() = scope
}

@OptIn(ExperimentalCoroutinesApi::class)
class TestViewModel(
    testMiddleware: TestMiddleware,
    testReducer: TestReducer,
    testScope: CoroutineScope
) : TestMviViewModel<TestState, TestEvent>(testScope) {

    override val state: FlowState<TestState> = FlowState(TestState())
    override val hub: TestFlowEventHub<TestEvent> = TestFlowEventHub(testScope)
    override val reducer: Reducer<TestEvent, TestState> = testReducer
    override val middleware: DslFlowMiddleware<TestEvent> = testMiddleware

    init {
        bindFlow()
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

class TestFlowEventHub<T : Event>(private val scope: CoroutineScope) : EventHub<T, Flow<T>> {

    private val hubFlow = MutableSharedFlow<T>()

    override fun observe(): Flow<T> {
        return hubFlow.asSharedFlow()
    }

    override suspend fun emit(event: T) {
        hubFlow.emit(event)
    }
}

abstract class TestMviViewModel<S : Any, E : Event>(private val testScope: CoroutineScope) :
    ViewModel(), TestFlowBinder {

    abstract val state: FlowState<S>
    abstract val hub: TestFlowEventHub<E>
    abstract val reducer: Reducer<E, S>
    abstract val middleware: DslFlowMiddleware<E>

    /** Must be called in descendant class `init` */
    fun bindFlow() {
        testScope.bind(hub, middleware, state, reducer)
    }
}

interface TestFlowBinder {

    fun <T : Event, SH> CoroutineScope.bind(
        eventHub: TestFlowEventHub<T>,
        middleware: Middleware<Flow<T>, Flow<T>>,
        stateHolder: SH,
        reactor: Reactor<T, SH>,
    ) {
        val eventFlow = eventHub.observe()
            .onEach { event: T ->
                reactor.react(stateHolder, event)
            }.catch {
                throw it
            }.shareIn(this, SharingStarted.Eagerly)
        launch {
            middleware.transform(eventFlow)
                .safeCollect { transformedEvent: T ->
                    eventHub.emit(transformedEvent)
                }
        }
    }
}

interface TestMVIView<S : Any, E : Event> {

    /**
     * Scope to observe on state changes and to emit events
     */
    val uiScope: CoroutineScope

    /**
     * viewModel providing event hub for event emission and observable state
     */
    val viewModel: TestMviViewModel<S, E>

    /**
     * Emits [event] to a hub which is provided by [viewModel]
     */
    fun emit(event: E) {
        uiScope.launch {
            viewModel.hub.emit(event)
        }
    }

    /**
     * Subscribes to state updates with the [collector]
     */
    @OptIn(InternalCoroutinesApi::class)
    fun observeState(collector: suspend (S) -> Unit) {
        uiScope.launch(Dispatchers.Main) {
            viewModel.state
                .observeState()
                .collect { collector(it) }
        }
    }
}