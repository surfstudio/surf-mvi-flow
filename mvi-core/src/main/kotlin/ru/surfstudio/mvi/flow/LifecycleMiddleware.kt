package ru.surfstudio.mvi.flow;

import android.util.Log
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.event.MviLifecycleEvent
import java.util.logging.Logger

/**
 * Middleware, that reacts on android lifecycle.
 *
 * To receive events, you need EventHub to implement interface [Lifecycle]
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
    .filter { it is MviLifecycleEvent && it.event == lifecycleEvent }
    .map { it }
