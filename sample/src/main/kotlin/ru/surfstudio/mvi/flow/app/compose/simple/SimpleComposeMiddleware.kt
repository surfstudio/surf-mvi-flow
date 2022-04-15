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
package ru.surfstudio.mvi.flow.app.compose.simple

import kotlinx.coroutines.flow.Flow
import ru.surfstudio.mvi.flow.DslFlowMiddleware
import ru.surfstudio.mvi.flow.app.compose.simple.SimpleComposeEvent.*

/**
 * Simple middleware for screen without state
 */
class SimpleComposeMiddleware : DslFlowMiddleware<SimpleComposeEvent> {

    override fun transform(eventStream: Flow<SimpleComposeEvent>): Flow<SimpleComposeEvent> {
        return eventStream.transformations {
            addAll(
                SimpleClickEventEvent::class eventToEvent { CommandEvents.ShowMessage },
            )
        }
    }
}