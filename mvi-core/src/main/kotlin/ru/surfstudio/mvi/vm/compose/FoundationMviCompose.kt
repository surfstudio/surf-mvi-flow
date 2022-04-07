package ru.surfstudio.mvi.vm.compose

import ru.surfstudio.mvi.core.event.Event


/** Helpful interface for emitting events from @Composable */
fun interface ComposedViewContext<E : Event> {
    fun emit(event: E)
}