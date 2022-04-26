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
package ru.surfstudio.mvi.vm.android

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.surfstudio.mvi.core.event.CommandEvent
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.vm.MviViewModel
import ru.surfstudio.mvi.vm.android.MviView
import ru.surfstudio.mvi.vm.compose.CommandObserver

/** Syntactic sugar fun for easy linking command events in [MviView] */
@SuppressLint("ComposableNaming")
infix fun <C : CommandEvent, E : Event, Vm> Vm.bindsCommandEvent(
    onCommandEventListener: CoroutineScope.(C) -> Unit
) where Vm : MviViewModel<E>, Vm : CommandObserver<E, C> {
    viewModelScope.launch {
        observeCommandEvents().onEach {
            this.onCommandEventListener(it)
        }.collect()
    }
}