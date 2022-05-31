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
import ru.surfstudio.mvi.mappers.*

class SimpleMappingTest {

    @Test
    fun testEmptyMapping() {
        val request = Request.Success("test")
        val requestUi = RequestUi<String>()
        val newRequestUi = RequestMapper.builder(request, requestUi).build()
        assert(!newRequestUi.hasData)
        assert(!newRequestUi.hasError)
        assert(!newRequestUi.isLoading)
    }

    @Test
    fun testSimpleMapping() {
        run { // no data at all, loading
            val request = Request.Loading<String>()
            val requestUi = RequestUi<String>()
            val newRequestUi = RequestMapper.builder(request, requestUi)
                .mapData(simpleDataMapper())
                .mapLoading(simpleLoadingMapper())
                .build()

            assert(!newRequestUi.hasData)
            assert(newRequestUi.isLoading)
        }

        run { // request succeed, no local data
            val request = Request.Success("newData")
            val requestUi = RequestUi<String>()
            val newRequestUi = RequestMapper.builder(request, requestUi)
                .mapData(simpleDataMapper())
                .mapLoading(simpleLoadingMapper())
                .build()

            assert(newRequestUi.data == "newData")
            assert(!newRequestUi.isLoading)
        }

        run { // request succeed, local data exists
            val request = Request.Success("newData")
            val requestUi = RequestUi("oldData")
            val newRequestUi = RequestMapper.builder(request, requestUi)
                .mapData(simpleDataMapper())
                .mapLoading(simpleLoadingMapper())
                .build()

            assert(newRequestUi.data == "newData")
            assert(!newRequestUi.isLoading)
        }

        run { // request failed, local data exists
            val request = Request.Error<String>(NoSuchElementException())
            val requestUi = RequestUi("oldData")
            val newRequestUi = RequestMapper.builder(request, requestUi)
                .mapData(simpleDataMapper())
                .mapLoading(simpleLoadingMapper())
                .build()

            assert(newRequestUi.data == "oldData")
            assert(!newRequestUi.isLoading)
        }

        run { // request failed, local data exists (another builder)
            val request = Request.Error<String>(NoSuchElementException())
            val newRequestUi = RequestMapper.builder(request, "oldData")
                .mapData(simpleDataMapper())
                .mapLoading(simpleLoadingMapper())
                .build()

            assert(newRequestUi.data == "oldData")
            assert(!newRequestUi.isLoading)
        }
    }
}