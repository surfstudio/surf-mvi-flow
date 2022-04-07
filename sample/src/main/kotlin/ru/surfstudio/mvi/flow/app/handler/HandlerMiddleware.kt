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
import ru.surfstudio.mvi.flow.app.reused.NetworkEvent
import ru.surfstudio.mvi.flow.app.reused.NetworkEvent.LoadDataRequest
import ru.surfstudio.mvi.flow.app.utils.mviFlow
import ru.surfstudio.mvi.mappers.MapperFlowMiddleware

class HandlerMiddleware(
    private val repository: IpRepository
) : MapperFlowMiddleware<NetworkEvent> {

    override fun transform(eventStream: Flow<NetworkEvent>): Flow<NetworkEvent> {
        return eventStream.transformations {
            addAll(
                // init loading
                flowOf(NetworkEvent.StartLoading),
                NetworkEvent.StartLoading::class eventToStream { loadData() }
            )
        }
    }

    private fun loadData(): Flow<NetworkEvent> =
        mviFlow { repository.getIpCountry() }
            .asRequestEvent(::LoadDataRequest)
}