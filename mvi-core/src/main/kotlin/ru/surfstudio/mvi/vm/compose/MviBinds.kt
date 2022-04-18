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

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.event.CommandEvent
import ru.surfstudio.mvi.vm.MviViewModel

/** Syntax sugar fun for convenient binding in @Composable with MVI */
@SuppressLint("ComposableNaming")
@Composable
infix fun <E : Event> MviViewModel<E>.binds(
    render: @Composable ComposedViewContext<E>.() -> Unit
) {
    val scope = rememberCoroutineScope()

    ComposedViewContext<E> { event ->
        scope.launch {
            hub.emit(event)
        }
    }.render()
}

/** Syntax sugar fun for convenient binding in @Composable with MVI */
@SuppressLint("ComposableNaming")
@Composable
fun <C : CommandEvent, E : Event> MviViewModel<E>.bindsCommandEvent(
    onCommandEventListener: CoroutineScope.(C) -> Unit
) {
    val scope = rememberCoroutineScope()
    bindsCommandEvent(scope = scope, onCommandEventListener = onCommandEventListener)
}

/** Syntax sugar fun for convenient binding in @Composable with MVI */
@SuppressLint("ComposableNaming", "CoroutineCreationDuringComposition")
@Composable
fun <C : CommandEvent, E : Event> MviViewModel<E>.bindsCommandEvent(
    scope: CoroutineScope,
    onCommandEventListener: CoroutineScope.(C) -> Unit
) {
    scope.launch {
        observeCommandEvents<E, C>().onEach {
            this.onCommandEventListener(it)
        }.collect()
    }
}