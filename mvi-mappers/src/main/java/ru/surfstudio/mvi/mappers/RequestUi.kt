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
 * Данные для отображения на Ui асинхронного запроса на получение данных.
 *
 * В отличие от Request, содержащего только состояние запроса в текущий момент времени
 * (либо загрузка, либо данные, либо ошибка),
 * этот класс содержит комбинацию из них
 * (загрузка + данные + ошибка),
 *
 * Кроме того, содержит информацию о том, как именно состояние загрузки данных
 * должно быть отображено на UI: [Loading]
 *
 * @param data  данные, либо их отсутствие
 * @param load  обертка над состоянием загрузки, интерпретируемая на ui
 * @param error состояние ошибки
 */
data class RequestUi<T>(
    val data: T? = null,
    val load: Loading? = null,
    val error: Throwable? = null
) {

    /**
     * Флаг, определяющий, выполняется ли запрос в данный момент.
     * */
    val isLoading: Boolean get() = load?.isLoading ?: false

    /**
     * Флаг, определяющий, есть ли данные в результате выполнения запроса.
     * */
    val hasData: Boolean get() = data != null

    /**
     * Флаг, определяющий, есть ли ошибка в результате выполнения запроса.
     * */
    val hasError: Boolean get() = error != null

}