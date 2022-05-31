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