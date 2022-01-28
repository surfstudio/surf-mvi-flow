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

import ru.surfstudio.mvi.flow.app.handler.error.*
import ru.surfstudio.mvi.mappers.*
import ru.surfstudio.mvi.mappers.handler.ErrorHandler

/**
 * Singleton-фабрика мапперов запросов.
 *
 * Используется для хранения переиспользуемых мапперов внутри проекта.
 *
 * todo обработка пагинации не реализована
 * */
object RequestMappers {

    /**
     * Мапперы данных запроса.
     * */
    val data = DataMappers

    /**
     * Мапперы состояния загрузки запроса.
     * */
    val loading = LoadingMappers

    /**
     * Обработчики ошибок запроса.
     * */
    val error = ErrorHandlers

    /**
     * Мапперы данных запроса.
     * */
    object DataMappers {

        /**
         * ## Маппер данных одиночного запроса.
         *
         * Маппер каждый новый запрос "очищает" данные в стейте.
         *
         * Следует использовать этот маппер в тех случаях, когда нам необходимо:
         *
         * * Сделать одиночный запрос и узнать результат его выполнения,
         * при этом нам не важно предыдущее его состояние.
         *
         * @return только что полученные данные из запроса.
         * */
        fun <T> single(): RequestDataMapper<T, T, T> = { request, _ ->
            when {
                request.isError && request.getError() is NonAuthorizedException -> null
                else -> request.getDataOrNull()
            }
        }

        /**
         * ## Маппер данных стандартного запроса.
         *
         * Следует использовать этот маппер в тех случаях, когда нам необходимо:
         *
         * * При первом запросе получить данные и сохранить их в стейт;
         * * При последующих запросах, если они удачные - обновить данные в стейте.
         *
         * @return только что полученные данные из запроса, либо данные из стейта.
         * */
        fun <T> default(): RequestDataMapper<T, T, T> = { request, data ->
            when {
                request.isError && request.getError() is NonAuthorizedException -> null
                else -> request.getDataOrNull() ?: data
            }
        }

        /**
         * ## Маппер данных списка
         *
         * Следует использовать этот маппер в тех случаях, когда нам необходимо:
         *
         * При получении данных сохранять их только в том случае если они не пустые.
         * При первом запросе получить данные и сохранить их в стейт;
         * При последующих запросах если не получили данные вернуть старые данные, если получили пустое значение
         * то удалить сохранные данные
         *
         * @return только что полученные данные из запроса (если придет пустой список то null), либо данные из стейта.
         */
        fun <T> emptyListToNull(): RequestDataMapper<List<T>, List<T>, List<T>> = { request, data ->
            when {
                request.isError && request.getError() is NonAuthorizedException -> null
                else -> {
                    if (request.getDataOrNull() == null) {
                        data
                    } else {
                        request.getData().takeIf { it.isNotEmpty() }
                    }
                }
            }
        }

        /**
         * ## Маппер данных запроса.
         *
         * При ошибке обнуляет данные, в остальном рабоает аналогично `default()`-мапперу.
         *
         * Подробное поведение:
         * * Если запрос **выполняется впервые, либо после ошибки** -- данные отсутствуют (`null`).
         * * Если запрос **выполнился успешно** -- данные сохраняются (`T`).
         * * Если запрос **выполняется повторно, после успеха** -- данные присутствуют (`T`).
         * * Если запрос **выполнился с ошибкой** -- данные удаляются (`null`).
         * */
        fun <T> keepUntilError(): RequestDataMapper<T, T, T> = { request, uiData ->
            when {
                request.isError -> null
                else -> request.getDataOrNull() ?: uiData
            }
        }
    }

    /**
     * Мапперы состояния загрузки запроса.
     * */
    object LoadingMappers {

        /**
         * ## Маппер состояния загрузки простого запроса.
         *
         * Следует использовать этот маппер в тех случаях, когда нам необходимо:
         *
         * * Получить состояние загрузки запроса в простом формате (загружается/не загружается).
         * * На экране осуществляется несколько запросов, на основании состояния загрузки которых
         * вычисляется все состояние экрана.
         *
         * @return актуальное, простое, состояние загрузки.
         * */
        fun <T1, T2> simple(): RequestLoadingMapper<T1, T2> =
            { request, _ -> SimpleLoading(request.isLoading) }

        /**
         * ## Маппер состояния загрузки стандартного запроса.
         *
         * Следует использовать этот маппер в тех случаях, когда нам необходимо:
         *
         * * На основании одного запроса - вычислить состояние всего экрана.
         *
         * @param isSwr является ли загрузка посредством PTR
         * @param showEmptyState нужно ли показывать [LoadStateType.Empty] при отсутствии данных
         *
         * @return актуальное состояние загрузки.
         */
        fun <T1, T2> default(
            isSwr: Boolean = false,
            showEmptyState: Boolean = true
        ): RequestLoadingMapper<T1, T2> = { request, data ->
            val hasData = when (data) {
                is List<*> -> data.isNotEmpty()
                null -> false
                else -> true
            }
            when {
                request.isLoading && hasData && isSwr -> LoadStateType.SwipeRefreshLoading
                request.isLoading && hasData -> LoadStateType.TransparentLoading
                request.isError && hasData -> LoadStateType.TransparentLoading
                request.isLoading && !hasData -> LoadStateType.Main
                request.isError && !hasData -> {
                    if (request.getError() is NoInternetException) {
                        LoadStateType.NoInternet
                    } else {
                        LoadStateType.Error
                    }
                }
                request.isSuccess && !hasData && showEmptyState -> LoadStateType.Empty
                else -> LoadStateType.None
            }
        }

        /**
         * ## Маппер состояния загрузки запроса.
         *
         * Подробное поведение:
         * * Запрос загружается с `isSwr`-флагом -- будет применен [LoadStateType.SwipeRefreshLoading];
         * * Запрос загружается -- будет применен [LoadStateType.Main];
         * * иначе [LoadStateType.None].
         *
         * @return актуальное состояние загрузки.
         */
        fun <T1, T2> mainOrNone(isSwr: Boolean = false): RequestLoadingMapper<T1, T2> =
            { request, _ ->
                when {
                    request.isLoading && isSwr -> LoadStateType.SwipeRefreshLoading
                    request.isLoading -> LoadStateType.Main
                    else -> LoadStateType.None
                }
            }

        /**
         * ## Маппер состояния загрузки запроса.
         *
         * Подробное поведение:
         * * Запрос загружается с `isSwr`-флагом -- будет применен [LoadStateType.SwipeRefreshLoading];
         * * Запрос загружается -- будет применен [LoadStateType.TransparentLoading];
         * * иначе [LoadStateType.None].
         *
         * @return актуальное состояние загрузки.
         */
        fun <T1, T2> transparentOrNone(isSwr: Boolean = false): RequestLoadingMapper<T1, T2> =
            { request, _ ->
                when {
                    request.isLoading && isSwr -> LoadStateType.SwipeRefreshLoading
                    request.isLoading -> LoadStateType.TransparentLoading
                    else -> LoadStateType.None
                }
            }
    }

    /**
     * Обработчики ошибок запроса.
     * */
    object ErrorHandlers {

        /**
         * ## Обработчик ошибок. Форсированный.
         *
         * Каждую из ошибок отправляет в [ErrorHandler] и завершает обработку ошибок для запроса.
         *
         * Следует использовать этот маппер в тех случая, когда нам необходимо:
         *
         * * Обработать все возникающие ошибки в [ErrorHandler]'е (например,
         * для одиночного запроса, у которого нету UI репрезентации).
         *
         * @return Была ли обработана ошибка? (Всегда -- true)
         * */
        fun <T> forced(errorHandler: ErrorHandler): RequestErrorHandler<T> = { error, _, _ ->
            error?.also(errorHandler::handleError)
            true
        }

        /**
         * ## Обработчик ошибок. Обработка в зависимости от текущего [LoadStateType].
         *
         * Обрабатывает ошибки только в том случае, когда экран не находится в состоянии ошибки.
         *
         * **Ошибки [NonAuthorizedException] и [NoInternetException] -- обрабатываются всегда!**
         *
         * @return Была ли обработана ошибка? (Всегда -- true)
         * */
        fun <T> loadingBased(errorHandler: ErrorHandler): RequestErrorHandler<T> =
            { error, _, loading ->
                val isSecurityProblem = error is NoInternetException
                val isNonAuthorized = error is NonAuthorizedException
                val isErrorStateOnDisplay = loading is LoadStateType.Error ||
                        loading is LoadStateType.NoInternet
                if (isSecurityProblem || isNonAuthorized || !isErrorStateOnDisplay) error?.also(
                    errorHandler::handleError
                )
                true
            }
    }
}