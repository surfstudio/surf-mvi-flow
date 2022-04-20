package ru.surfstudio.mvi.core.event

import androidx.lifecycle.Lifecycle

/** Screen lifecycle event */
interface LifecycleMviEvent : Event {
    var event: Lifecycle.Event
}
