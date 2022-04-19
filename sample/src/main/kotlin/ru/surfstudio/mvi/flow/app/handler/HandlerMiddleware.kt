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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.surfstudio.mvi.flow.app.network.IpRepository
import ru.surfstudio.mvi.flow.app.reused.NetworkCommandEvents
import ru.surfstudio.mvi.flow.app.reused.NetworkEvent
import ru.surfstudio.mvi.flow.app.reused.NetworkEvent.*
import ru.surfstudio.mvi.flow.app.reused.NetworkEvent.LoadDataRequest
import ru.surfstudio.mvi.flow.app.utils.mviFlow
import ru.surfstudio.mvi.mappers.MapperFlowMiddleware
import ru.surfstudio.mvi.vm.compose.CommandEmmiter

class HandlerMiddleware(
    private val repository: IpRepository,
    override val emitCommandCallback: (NetworkCommandEvents) -> Unit,
) : MapperFlowMiddleware<NetworkEvent>, CommandEmmiter<NetworkCommandEvents> {

    override fun transform(eventStream: Flow<NetworkEvent>): Flow<NetworkEvent> {
        return eventStream.transformations {
            addAll(
                // init loading
                flowOf(StartLoading),
                StartLoading::class eventToStream { loadData() },
                LoadDataRequest::class filter { !it.isLoading } react {
                    emitCommandCallback(NetworkCommandEvents.ShowSnackSuccessLoading)
                },
            )
        }
    }

    private fun loadData(): Flow<NetworkEvent> =
        mviFlow { repository.getIpCountry() }
            .asRequestEvent(::LoadDataRequest)
}