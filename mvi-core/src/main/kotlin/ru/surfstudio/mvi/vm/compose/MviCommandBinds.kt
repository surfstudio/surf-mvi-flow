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
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import ru.surfstudio.mvi.core.event.CommandEvent
import ru.surfstudio.mvi.core.event.Event

/** Syntactic sugar fun for easy linking command events in @Composable */
@SuppressLint("ComposableNaming")
@Composable
infix fun <C : CommandEvent, E : Event> CommandObserver<E, C>.bindsCommandEvent(
    onCommandEventListener: CoroutineScope.(C) -> Unit
) {
    LaunchedEffect(Unit) {
        observeCommandEvents().onEach {
            onCommandEventListener(it)
        }.collect()
    }
}
