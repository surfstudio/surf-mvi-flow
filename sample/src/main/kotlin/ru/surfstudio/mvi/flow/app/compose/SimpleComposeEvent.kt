package ru.surfstudio.mvi.flow.app.compose

import ru.surfstudio.mvi.core.event.Event

/**
 * Event for compose screen without state
 */
sealed class SimpleComposeEvent : Event {
    object SimpleClickEventEvent : SimpleComposeEvent()
}