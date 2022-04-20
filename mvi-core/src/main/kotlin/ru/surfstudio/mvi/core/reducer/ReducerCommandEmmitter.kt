package ru.surfstudio.mvi.core.reducer

import ru.surfstudio.mvi.core.event.CommandEvent

/** Helpful interface for emitting command event from [Reducer] */
interface ReducerCommandEmmitter<C : CommandEvent> {
    val emitCommand: (C) -> Unit
}
