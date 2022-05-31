package ru.surfstudio.mvi.mappers.test

import ru.surfstudio.mvi.mappers.RequestDataMapper
import ru.surfstudio.mvi.mappers.RequestLoadingMapper
import ru.surfstudio.mvi.mappers.SimpleLoading

internal fun <T> simpleDataMapper(): RequestDataMapper<T, T, T> = { request, data ->
    request.getDataOrNull() ?: data
}

internal fun <T> simpleLoadingMapper(): RequestLoadingMapper<T, T> = { request, _ ->
    SimpleLoading(request.isLoading)
}