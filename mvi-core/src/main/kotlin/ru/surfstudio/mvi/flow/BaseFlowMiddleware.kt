package ru.surfstudio.mvi.flow

import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.lifecycle.MapperLifecycleEvent

/**
 * Base middleware for applications with support for lifecycle events
 */
interface BaseFlowMiddleware<E : Event> : LifecycleMiddleware<E>, MapperLifecycleEvent<E>