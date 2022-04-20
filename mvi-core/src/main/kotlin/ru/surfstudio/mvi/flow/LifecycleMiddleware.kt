package ru.surfstudio.mvi.flow;

import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.event.LifecycleMviEvent

/**
 * Middleware, that reacts on android lifecycle.
 *
 * To receive events, you need to add event that implements [LifecycleMviEvent]
 * and for your viewModel implements [MapperLifecycleEvent]
 * The last step is to call the [bindLifecycleEvent]
 * for the entity implementing the MviView interface
 */
interface LifecycleMiddleware<T : Event> : DslFlowMiddleware<T> {
    fun Flow<T>.onCreate() = filterLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun Flow<T>.onStart() = filterLifecycleEvent(Lifecycle.Event.ON_START)
    fun Flow<T>.onStop() = filterLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun Flow<T>.onResume() = filterLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun Flow<T>.onDestroy() = filterLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun Flow<T>.onPause() = filterLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun Flow<T>.onAny() = filterLifecycleEvent(Lifecycle.Event.ON_ANY)

    fun EventTransformerList<T>.onCreate() = eventStream.onCreate()
    fun EventTransformerList<T>.onStart() = eventStream.onStart()
    fun EventTransformerList<T>.onResume() = eventStream.onResume()
    fun EventTransformerList<T>.onPause() = eventStream.onPause()
    fun EventTransformerList<T>.onStop() = eventStream.onStop()
    fun EventTransformerList<T>.onDestroy() = eventStream.onDestroy()
    fun EventTransformerList<T>.onAny() = eventStream.onAny()
}

private fun <T> Flow<T>.filterLifecycleEvent(lifecycleEvent: Lifecycle.Event) = this
    .filter { it is LifecycleMviEvent && it.event == lifecycleEvent }
    .map { it }
