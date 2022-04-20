package ru.surfstudio.mvi.core.event

import androidx.lifecycle.Lifecycle

/** Screen lifecycle event */
interface MviLifecycleEvent  {
    var event: Lifecycle.Event
}
