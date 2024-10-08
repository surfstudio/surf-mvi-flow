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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * Расширение для Flow, переводящее асинхронный запрос загрузки данных к Flow<[Request]>.
 *
 * При добавлении к цепочке flow, необходимо применять именно к тому элементу, который будет эмитить значения.
 */
fun <T> Flow<T>.asRequest(): Flow<Request<T>> = this
    .map { Request.Success(it) as Request<T> }
    .onStart { emit(Request.Loading()) }
    .catch { emit(Request.Error(it)) }