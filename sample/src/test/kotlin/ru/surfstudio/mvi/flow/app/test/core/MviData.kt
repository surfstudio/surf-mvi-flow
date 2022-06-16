package ru.surfstudio.mvi.flow.app.test.core

import ru.surfstudio.mvi.core.event.Event

/**
 * Data used for tests for sequential representation of events and states transmission.
 * Always has either event or state, but never two non-null values.
 */
data class MviData<S : Any, E : Event>(
    val event: E? = null,
    val state: S? = null
)