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
 * Обертка для отображения состояния загрузки на Ui.
 *
 * Можно реализовать у себя в проекте все необходимые состояния, унаследовавшись от этого класса.
 */
interface Loading {
    val isLoading: Boolean
}

/**
 * Состояние загрузки, используемое для кейсов, в которых важен сам факт осуществления загрузки.
 * */
data class SimpleLoading(override val isLoading: Boolean) : Loading

/**
 * Состояние загрузки, когда на экране нет контента
 */
class MainLoading(override val isLoading: Boolean) : Loading

/**
 * Состояние загрузки, когда на экране есть какой-то контент
 */
class TransparentLoading(override val isLoading: Boolean) : Loading

/**
 * Состояние загрузки при вызове через SwipeRefresh
 */
class SwipeRefreshLoading(override val isLoading: Boolean) : Loading