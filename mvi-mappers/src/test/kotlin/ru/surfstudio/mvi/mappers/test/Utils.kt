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
package ru.surfstudio.mvi.mappers.test

import ru.surfstudio.mvi.mappers.Loading
import ru.surfstudio.mvi.mappers.RequestDataMapper
import ru.surfstudio.mvi.mappers.RequestLoadingMapper
import ru.surfstudio.mvi.mappers.SimpleLoading

internal fun <T> simpleDataMapper(): RequestDataMapper<T, T, T> = { request, data ->
    request.getDataOrNull() ?: data
}

internal fun <T> simpleLoadingMapper(): RequestLoadingMapper<T, T> = { request, _ ->
    SimpleLoading(request.isLoading)
}

internal enum class LoadStateType(override val isLoading: Boolean) : Loading {
    Loading(true),
    None(false),
    Error(false)
}