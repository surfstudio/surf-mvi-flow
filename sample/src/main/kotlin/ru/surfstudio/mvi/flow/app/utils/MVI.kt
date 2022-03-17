package ru.surfstudio.mvi.flow.app.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * App util for making a [Flow] from a suspend api fun
 * for usage in middleware for further mapping
 */
fun <D: Any> mviFlow(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    value: suspend () -> D
): Flow<D> {
    return flow {
        delay(2000L) // test
        this.emit(value()) // propagate exception in order to handle it further
    }
        .flowOn(dispatcher)
        .catch {
            println(it)
            throw it
        }
}