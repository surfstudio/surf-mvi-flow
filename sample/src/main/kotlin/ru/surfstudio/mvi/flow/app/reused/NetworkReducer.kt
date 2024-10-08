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
package ru.surfstudio.mvi.flow.app.reused

import ru.surfstudio.mvi.core.reducer.ReducerCommandEmmitter
import ru.surfstudio.mvi.flow.app.reused.mapper.LoadStateType
import ru.surfstudio.mvi.flow.app.reused.mapper.RequestMappers
import ru.surfstudio.mvi.mappers.RequestEvent
import ru.surfstudio.mvi.mappers.RequestMapper
import ru.surfstudio.mvi.mappers.RequestUi
import ru.surfstudio.mvi.mappers.handler.ErrorHandler
import ru.surfstudio.mvi.mappers.handler.ErrorHandlerReducer

data class NetworkState(
    val dataRequestUi: RequestUi<String> = RequestUi()
) {
    private val loadState: LoadStateType =
        dataRequestUi.load as? LoadStateType ?: LoadStateType.None
    private val data: String = dataRequestUi.data ?: "Initial"

    val loadStateData: String = when (loadState) {
        LoadStateType.Empty -> "Empty state"
        LoadStateType.Error -> "Error"
        LoadStateType.Main -> "Main loading"
        LoadStateType.NoInternet -> "No internet"
        LoadStateType.None -> data
        LoadStateType.SwipeRefreshLoading -> "Swipe refresh"
        LoadStateType.TransparentLoading -> "Transparent loading"
    }
}

class NetworkReducer(
    override val errorHandler: ErrorHandler,
    override val emitCommand: (NetworkCommandEvent) -> Unit,
) : ErrorHandlerReducer<NetworkEvent, NetworkState>,
    ReducerCommandEmmitter<NetworkCommandEvent> {

    override fun reduce(state: NetworkState, event: NetworkEvent): NetworkState {
        return when (event) {
            is NetworkEvent.DoNothingAndScrollToBottom -> scrollToBottomCommand(state)
            is NetworkEvent.LoadDataRequest -> state.copy(
                dataRequestUi = updateRequestUi(event, state.dataRequestUi)
            )
            else -> state
        }
    }

    private fun scrollToBottomCommand(state: NetworkState): NetworkState {
        emitCommand.invoke(NetworkCommandEvent.ScrollToBottom)
        return state
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