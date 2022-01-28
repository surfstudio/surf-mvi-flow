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

import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.flow.app.handler.error.ErrorHandlerImpl
import ru.surfstudio.mvi.flow.app.network.IpNetworkCreator
import ru.surfstudio.mvi.mappers.handler.MviErrorHandlerViewModel

class HandlerViewModel : MviErrorHandlerViewModel<HandlerState, HandlerEvent>() {

    override val state: FlowState<HandlerState> = FlowState(HandlerState())
    override val hub: FlowEventHub<HandlerEvent> = FlowEventHub()
    override val middleware: HandlerMiddleware =
        HandlerMiddleware(IpNetworkCreator.repository)
    override val reducer: HandlerReducer = HandlerReducer(ErrorHandlerImpl())

    init {
        bindFlow()
    }
}