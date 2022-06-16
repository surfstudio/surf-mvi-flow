package ru.surfstudio.mvi.flow.app.test

import org.junit.Test
import ru.surfstudio.mvi.flow.app.simple.SimpleEvent
import ru.surfstudio.mvi.flow.app.simple.SimpleViewModel
import ru.surfstudio.mvi.flow.app.simple.request.RequestState
import ru.surfstudio.mvi.flow.app.test.core.BaseMviScreenTest
import ru.surfstudio.mvi.flow.app.test.core.BaseMviScreenTest.ViewModelProducer

class SimpleScreenTest : BaseMviScreenTest() {

    private val viewModelProducer = ViewModelProducer { SimpleViewModel() }

    @Test
    fun `test multiple SimpleClick emits map to a single TitleUpdate`() {
        val startEvent = SimpleEvent.SimpleClick
        viewModelProducer.produce().hub.middlewareTest(
            startEvents = listOf(startEvent, startEvent, startEvent),
            expectedFinalEventCheck = { it is SimpleEvent.TitleUpdate }
        )
    }

    @Test
    fun `test start loading click mappings`() {
        val startEvent = SimpleEvent.StartLoadingClick
        viewModelProducer.produce().hub.middlewareTest(
            startEvent = startEvent,
            expectedEventsChecks = listOf(
                { it == startEvent },
                { (it as SimpleEvent.RequestEvent).request == RequestState.Loading },
                // skip random error or success check
                { true },
                { (it as SimpleEvent.RequestEvent).request == RequestState.None }
            )
        )
    }
}