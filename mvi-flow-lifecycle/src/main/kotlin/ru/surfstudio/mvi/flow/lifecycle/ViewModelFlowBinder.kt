package ru.surfstudio.mvi.flow.lifecycle

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.middleware.Middleware
import ru.surfstudio.mvi.core.reducer.Reducer
import ru.surfstudio.mvi.flow.FlowBinder
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.MutableFlowStateHolder

class ViewModelFlowBinder<T : Event, S: Any>(
    private val eventHub: FlowEventHub<T>,
    private val middleware: Middleware<Flow<T>, Flow<T>>,
    private val stateHolder: MutableFlowStateHolder<S>,
    private val reactor: Reducer<T, S>
): FlowBinder {

    /**
     * Binds everything together in passed [scope]
     */
    override fun bindFlowIn(scope: CoroutineScope) {
        val eventFlow = eventHub.observe()
            .onEach { event: T ->
                Log.d(TAG, event.toString())
                reactor.react(stateHolder, event)
            }.catch {
                Log.e(TAG, it.message, it)
                throw it
            }.shareIn(scope, SharingStarted.Eagerly)
        scope.launch {
            middleware.transform(eventFlow)
                .collect { transformedEvent: T ->
                    eventHub.emit(transformedEvent)
                }
        }
    }

    companion object {
        const val TAG = "FlowBinder"
    }
}