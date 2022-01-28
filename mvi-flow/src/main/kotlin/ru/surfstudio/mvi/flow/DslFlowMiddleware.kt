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

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.middleware.DslMiddleware

/**
 * Transforms Flow streams using the transformations described in the [EventTransformerList].
 */
interface DslFlowMiddleware<T : Event> : DslMiddleware<Flow<T>, Flow<T>, EventTransformerList<T>> {

    override fun provideTransformationList(eventStream: Flow<T>): EventTransformerList<T> {
        return EventTransformerList(eventStream)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun combineTransformations(transformations: List<Flow<T>>): Flow<T> {
        return merge(*transformations.toTypedArray())
    }
}