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
package ru.surfstudio.mvi.flow.app.simple

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowStateHolder
import ru.surfstudio.mvi.flow.lifecycle.ViewModelFlowBinder

class SimpleViewModelFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val stateHolder = FlowStateHolder(SimpleState())
        val hub = FlowEventHub<SimpleEvent>()
        val middleware = SimpleMiddleware(stateHolder)
        val reducer = SimpleReducer()

        val binder = ViewModelFlowBinder(hub, middleware, stateHolder, reducer)
        val simpleViewModel = SimpleViewModel(stateHolder, hub)

        binder.bindFlowIn(simpleViewModel.viewModelScope)

        return simpleViewModel as T
    }
}