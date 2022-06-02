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
        var isSpecificErrorCaught = false
        val request = Request.Error<String>(NoSuchElementException())
        val newRequestUi = RequestMapper.builder(request)
            .handleSpecificError(NoSuchElementException::class) { _, _, _ ->
                isSpecificErrorCaught = true
                true
            }
            .handleError { _, _, _ ->
                // unreachable because of prior specific handler
                isSpecificErrorCaught = false
                true
            }
            .build()

        assert(newRequestUi.error is NoSuchElementException)
        assert(isSpecificErrorCaught)
    }
}