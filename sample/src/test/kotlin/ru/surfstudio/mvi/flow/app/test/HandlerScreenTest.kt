package ru.surfstudio.mvi.flow.app.test

import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test
import ru.surfstudio.mvi.flow.app.handler.HandlerViewModel
import ru.surfstudio.mvi.flow.app.network.IpRepository
import ru.surfstudio.mvi.flow.app.reused.NetworkCommandEvent
import ru.surfstudio.mvi.flow.app.reused.NetworkEvent
import ru.surfstudio.mvi.flow.app.test.core.BaseMviScreenTest
import ru.surfstudio.mvi.flow.app.test.core.BaseMviScreenTest.ViewModelProducer

class HandlerScreenTest : BaseMviScreenTest() {

    private val repository: IpRepository = mockk {
        coEvery { getIpCountry() } returns "TEST"
    }
    private val viewModelProducer = ViewModelProducer {
        HandlerViewModel(loadOnStart = false, repository = repository, dispatcher = testDispatcher)
    }

    @Test
    fun `test start loading mapping`() {
        val startEvent = NetworkEvent.StartLoading
        viewModelProducer.produce().hub.middlewareTest(
            startEvent = startEvent,
            expectedEventsChecks = listOf(
                { it == startEvent },
                { (it as NetworkEvent.LoadDataRequest).isLoading }
            )
        )
    }

    @Test
    fun `test show snack bar on succeed`() {
        val startEvent = NetworkEvent.StartLoading
        viewModelProducer.produce().hub.middlewareTest(
            startEvents = listOf(startEvent),
            expectedFinalEventCheck = {
                it == NetworkCommandEvent.ShowSnackSuccessLoading
            }
        )
    }
}