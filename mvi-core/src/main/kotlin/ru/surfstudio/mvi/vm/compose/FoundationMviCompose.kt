/*
 * Copyright 2022 Surf LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.surfstudio.mvi.vm.compose

import ru.surfstudio.mvi.core.event.CommandEvent
import ru.surfstudio.mvi.core.event.Event


/** Helpful interface for emitting events from @Composable */
fun interface ComposedViewContext<E : Event> {
    fun emit(event: E)
}

/**
 *  Helpful interface for emitting command event from [Reducer]
 */
 interface CommandEmmiter<E> where E : Event, E : CommandEvent  {
    val emitCommandCallback: (E) -> Unit
}
