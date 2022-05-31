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