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
package ru.surfstudio.mvi.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.surfstudio.mvi.core.state.MutableState

/**
 * State implementing with Coroutines Flow, that could be observed and changed.
 */
class FlowState<S>(initialState: S): MutableState<S, Flow<S>> {

    private val mutableFlow = MutableStateFlow(initialState)

    override val currentState: S
        get() = mutableFlow.value

    override fun emitNewState(newState: S) {
        mutableFlow.value = newState
    }

    override fun observeState(): Flow<S> {
        return mutableFlow
    }
}