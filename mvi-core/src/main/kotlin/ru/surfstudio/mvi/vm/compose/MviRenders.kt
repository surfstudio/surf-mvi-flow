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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.vm.MviStatefulViewModel

/** Syntax sugar fun for convenient binding in @Composable with MVI */
@SuppressLint("ComposableNaming")
@Composable
infix fun <S : Any, E : Event> MviStatefulViewModel<S, E>.renders(
    render: @Composable ComposedViewContext<E>.(S) -> Unit
) {
    val state by stateHolder.observeState().collectAsState(initial = stateHolder.currentState)
    val scope = rememberCoroutineScope()

    val composedViewContext = ComposedViewContext<E> { event ->
        scope.launch {
            hub.emit(event)
        }
    }
    composedViewContext.render(state)
}