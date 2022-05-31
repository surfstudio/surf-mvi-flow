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

class RequestTypeTransformationTest {

    @Test
    fun testRequestTypeTransformation() {
        run { // string to int transformation
            val request = Request.Success("123")
            val requestUi = RequestUi(456)
            val newRequestUi = RequestMapper.builder(request, requestUi)
                .mapRequest { data -> data.toIntOrNull() ?: 0 }
                .mapData(simpleDataMapper())
                .mapLoading(simpleLoadingMapper())
                .build()

            assert(newRequestUi.data == 123)
            assert(!newRequestUi.isLoading)
        }

        run { // list of strings to list of ints transformation
            val request = Request.Success(listOf("1", "2", "3"))
            val requestUi = RequestUi<List<Int>>()
            val newRequestUi = RequestMapper.builder(request, requestUi)
                .mapRequest { data -> data.map { it.toIntOrNull() ?: 0 } }
                .mapData(simpleDataMapper())
                .mapLoading(simpleLoadingMapper())
                .build()

            assert(newRequestUi.data is List<Int>)
            assert(newRequestUi.data?.get(0) == 1)
            assert(newRequestUi.data?.get(1) == 2)
            assert(newRequestUi.data?.get(2) == 3)
            assert(!newRequestUi.isLoading)
        }
    }
}