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

class ReactMappingTest {

    @Test
    fun testReact() {
        run { // test react loading
            var reactHandled = false
            val initialRequest = Request.Loading<Unit>()
            RequestMapper.builder(initialRequest)
                .mapLoading(simpleLoadingMapper())
                .react { request, _, loading ->
                    reactHandled = true
                    assert(request.isLoading)
                    assert(loading!!.isLoading)
                }
                .build()
            assert(reactHandled)
        }

        run { // test react data
            var reactHandled = false
            val initialRequest = Request.Success("data")
            RequestMapper.builder(initialRequest)
                .mapData(simpleDataMapper())
                .react { request, data, _ ->
                    reactHandled = true
                    assert(request.isSuccess)
                    assert(data == "data")
                }
                .build()
            assert(reactHandled)
        }
    }

    @Test
    fun testSpecificReact() {
        run { // test react loading
            var reactHandled = false
            val initialRequest = Request.Loading<Unit>()
            RequestMapper.builder(initialRequest)
                .mapLoading(simpleLoadingMapper())
                .reactOnLoading { loading ->
                    reactHandled = true
                    assert(loading!!.isLoading)
                }
                .build()
            assert(reactHandled)
        }

        run { // test react success
            var reactHandled = false
            val initialRequest = Request.Success("data")
            RequestMapper.builder(initialRequest)
                .mapData(simpleDataMapper())
                .reactOnSuccess { data ->
                    reactHandled = true
                    assert(data == "data")
                }
                .build()
            assert(reactHandled)
        }

        run { // test react error
            var reactHandled = false
            val initialRequest = Request.Error<Unit>(NoSuchElementException())
            RequestMapper.builder(initialRequest)
                .mapData(simpleDataMapper())
                .reactOnError { error ->
                    reactHandled = true
                    assert(error is NoSuchElementException)
                }
                .build()
            assert(reactHandled)
        }
    }
}