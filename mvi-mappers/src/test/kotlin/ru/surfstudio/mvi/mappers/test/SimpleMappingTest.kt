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