package ru.surfstudio.mvi.mappers.test

import org.junit.Test
import ru.surfstudio.mvi.mappers.Request
import ru.surfstudio.mvi.mappers.RequestMapper
import ru.surfstudio.mvi.mappers.RequestUi

class ErrorMappingTest {

    @Test
    fun testSimpleErrorHandling() {
        var isErrorHandled = false
        val request = Request.Error<String>(NoSuchElementException())
        val requestUi = RequestUi("oldData")
        val newRequestUi = RequestMapper.builder(request, requestUi)
            .mapData(simpleDataMapper())
            .handleError { error, _, _ ->
                isErrorHandled = error is NoSuchElementException
                true
            }
            .build()

        assert(newRequestUi.data == "oldData")
        assert(!newRequestUi.isLoading)
        assert(newRequestUi.error is NoSuchElementException)
        assert(isErrorHandled)
    }

    @Test
    fun testSpecificErrorHandling() {
        var isErrorHandled = false
        val request = Request.Error<String>(NoSuchElementException())
        val newRequestUi = RequestMapper.builder(request)
            .handleSpecificError(NoSuchElementException::class) { _, _, _ ->
                isErrorHandled = true
                true
            }
            .build()

        assert(newRequestUi.error is NoSuchElementException)
        assert(isErrorHandled)
    }

    @Test
    fun testErrorBlocksPassing() {
        var isSpecificErrorCatched = false
        val request = Request.Error<String>(NoSuchElementException())
        val newRequestUi = RequestMapper.builder(request)
            .handleSpecificError(NoSuchElementException::class) { _, _, _ ->
                isSpecificErrorCatched = true
                true
            }
            .handleError { _, _, _ ->
                // unreachable because of prior specific handler
                isSpecificErrorCatched = false
                true
            }
            .build()

        assert(newRequestUi.error is NoSuchElementException)
        assert(isSpecificErrorCatched)
    }
}