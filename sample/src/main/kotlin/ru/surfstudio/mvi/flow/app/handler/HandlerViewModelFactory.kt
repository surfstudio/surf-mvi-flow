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
package ru.surfstudio.mvi.flow.app.handler

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowStateHolder
import ru.surfstudio.mvi.flow.app.handler.error.ErrorHandlerImpl
import ru.surfstudio.mvi.flow.app.network.IpNetworkCreator
import ru.surfstudio.mvi.flow.lifecycle.ViewModelFlowBinder

class HandlerViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val stateHolder = FlowStateHolder(HandlerState())
        val hub: FlowEventHub<HandlerEvent> = FlowEventHub()
        val middleware = HandlerMiddleware(IpNetworkCreator.repository)
        val reducer = HandlerReducer(ErrorHandlerImpl())

        val viewModel = HandlerViewModel(stateHolder, hub)

        val binder = ViewModelFlowBinder(hub, middleware, stateHolder, reducer)
        binder.bindFlowIn(viewModel.viewModelScope)

        return viewModel as T
    }

}