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
package ru.surfstudio.mvi.mappers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.event.EventFactory
import ru.surfstudio.mvi.flow.DslFlowMiddleware

/**
 * Base middleware implementation which supports mappers
 */
interface MapperFlowMiddleware<T : Event> : DslFlowMiddleware<T> {

    /**
     * Расширение для Flow, переводящее асинхронный запрос загрузки данных к Observable<[Flow]>.
     *
     * При добавлении к цепочке flow, необходимо применять именно к тому элементу, который будет эмитить значения.
     */
    fun <T, E : RequestEvent<T>> Flow<T>.asRequestEvent(
        eventFactory: EventFactory<Request<T>, E>
    ): Flow<E> = this
        .asRequest()
        .map { request -> eventFactory(request) }

    /**
     * Аналогично `asRequestEvent`, кроме того, переводит работу в фоновый поток
     */
    fun <T, E : RequestEvent<T>> Flow<T>.asIoRequestEvent(
        eventFactory: EventFactory<Request<T>, E>
    ): Flow<E> = this
        .asRequest()
        .map { request -> eventFactory(request) }
        .flowOn(Dispatchers.IO)

    /**
     * Пропуск дальнейшего проброса события.
     */
    fun <T> skip(): Flow<T> = emptyFlow()

    /**
     * Выполнение действия и пропуск дальнейшего проброса события.
     */
    fun <T> doAndSkip(action: () -> Unit): Flow<T> {
        action()
        return skip()
    }

    /**
     * @see kotlinx.coroutines.flow.merge
     */
    fun merge(vararg flows: Flow<T>): Flow<T> =
        kotlinx.coroutines.flow.merge(*flows.toList().toTypedArray())

    /**
     * @see emptyFlow
     */
    fun <T> Any?.skip(): Flow<T> = emptyFlow()
}