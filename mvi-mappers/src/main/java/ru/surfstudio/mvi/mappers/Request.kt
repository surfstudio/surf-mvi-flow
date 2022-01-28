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

/**
 * Тип загрузки данных уровня Interactor.
 *
 * Отражает цикл асинхронной загрузки данных:
 *
 * 1. Получение данных начинается с состояния загрузки [Request.Loading],
 * 2. После этого либо приходят данные [Request.Success],
 * 3. Либо приходит ошибка загрузки данных [Request.Error]
 */
sealed class Request<T> {

    class Loading<T> : Request<T>()
    data class Success<T>(internal val data: T) : Request<T>()
    data class Error<T>(internal val error: Throwable) : Request<T>()

    /**
     * Флаг, определяющий, выполняется ли запрос в данный момент.
     */
    val isLoading: Boolean get() = this is Loading

    /**
     * Флаг, определяющий, выполнился ли запрос успешно.
     */
    val isSuccess: Boolean get() = this is Success

    /**
     * Флаг, определяющий, выполнился ли запрос с ошибкой.
     */
    val isError: Boolean get() = this is Error

    /**
     * Извлечение данных, полученных в результате выполнения запроса.
     *
     * **Будет выброшено исключение,
     * если данные извлекаются из экземпляра [Request], отличного от [Request.Success].**
     */
    fun getData(): T = (this as Success).data

    /**
     * Безопасное извлечение данных, полученных в результате выполнения запроса.
     * В случае отсутствия данных будет возвращен null.
     */
    fun getDataOrNull(): T? = (this as? Success)?.data

    /**
     * Извлечение ошибки, полученной в результате выполнения запроса.
     *
     * **Будет выброшено исключение,
     * если ошибка извлекается из экземпляра [Request], отличного от [Request.Error].**
     */
    fun getError(): Throwable = (this as Error).error

    /**
     * Безопасное извлечение ошибки, полученной в результате выполнения запроса.
     * В случае отсутствия ошибки будет возвращен null.
     */
    fun getErrorOrNull(): Throwable? = (this as? Error)?.error

    /**
     * Трансформация Request<T> в Request<R>.
     *
     * @param mapper функция трансформации данных запроса.
     */
    fun <R> map(mapper: (T) -> R): Request<R> {
        return when (this) {
            is Success -> Success(mapper(data))
            is Error -> Error(error)
            is Loading -> Loading()
        }
    }
}