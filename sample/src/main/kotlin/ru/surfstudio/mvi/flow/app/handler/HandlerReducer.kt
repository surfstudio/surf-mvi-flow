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

import ru.surfstudio.mvi.flow.app.handler.mapper.LoadStateType
import ru.surfstudio.mvi.flow.app.handler.mapper.RequestMappers
import ru.surfstudio.mvi.mappers.RequestEvent
import ru.surfstudio.mvi.mappers.RequestMapper
import ru.surfstudio.mvi.mappers.RequestUi
import ru.surfstudio.mvi.mappers.handler.ErrorHandler
import ru.surfstudio.mvi.mappers.handler.ErrorHandlerReducer

data class HandlerState(
    val dataRequestUi: RequestUi<String> = RequestUi()
) {
    val loadState: LoadStateType = dataRequestUi.load as? LoadStateType ?: LoadStateType.Main
    val data: String = dataRequestUi.data.orEmpty()
}

class HandlerReducer(
    override val errorHandler: ErrorHandler
) : ErrorHandlerReducer<HandlerEvent, HandlerState> {

    override fun reduce(state: HandlerState, event: HandlerEvent): HandlerState {
        return when (event) {
            is HandlerEvent.LoadDataRequest -> state.copy(
                dataRequestUi = updateRequestUi(event, state.dataRequestUi)
            )
            else -> state
        }
    }

    private fun <T : Any> updateRequestUi(
        event: RequestEvent<T>,
        requestUi: RequestUi<T>
    ): RequestUi<T> {
        return RequestMapper
            .builder(event.request, requestUi)
            .mapData(RequestMappers.data.default())
            .mapLoading(RequestMappers.loading.default())
            .handleError(RequestMappers.error.forced(errorHandler))
            .build()
    }
}