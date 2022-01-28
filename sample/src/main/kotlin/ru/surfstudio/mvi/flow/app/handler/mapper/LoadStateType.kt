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
package ru.surfstudio.mvi.flow.app.handler.mapper

import ru.surfstudio.mvi.mappers.Loading

sealed class LoadStateType : Loading {

    /**
     * Отсутствие загрузки в плейсхолдере, плейсхолдер скрыт
     */
    object None : LoadStateType() {
        override val isLoading: Boolean = false
    }

    /**
     * Стейт активного PTR
     */
    object SwipeRefreshLoading : LoadStateType() {
        override val isLoading: Boolean = true
    }

    /**
     * Контейнер с ошибкой и возможностью перезагрузить данные.
     */
    object Error : LoadStateType() {
        override val isLoading: Boolean = false
    }

    /**
     * Контейнер с ошибкой об отсутствии интернета и возможностью перезагрузить данные.
     */
    object NoInternet : LoadStateType() {
        override val isLoading: Boolean = false
    }

    /**
     * Контейнер с сообщением о том, что мы получили пустой список данных
     */
    object Empty : LoadStateType() {
        override val isLoading: Boolean = false
    }

    /**
     * Полноэкранный лоадер, отображается поверх контента
     */
    object Main : LoadStateType() {
        override val isLoading: Boolean = true
    }

    /**
     * Прозрачный лоадер, за которым видно контент.
     */
    object TransparentLoading : LoadStateType() {
        override val isLoading: Boolean = true
    }
}