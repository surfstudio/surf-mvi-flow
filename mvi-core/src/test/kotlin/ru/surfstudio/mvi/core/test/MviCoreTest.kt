package ru.surfstudio.mvi.core.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MviCoreTest : BaseFlowTest() {

    @Test
    fun testEventsPassingMiddleware() = runTest {
        Assert.assertEquals(0, middleware.eventsCount)

        testView?.emit(TestEvent.Ui)
        testView?.emit(TestEvent.Logic)
        testView?.emit(TestEvent.Data(""))

        Assert.assertEquals(3, middleware.eventsCount)
    }

    @Test
    fun testEventsPassingReducer() = runTest {
        Assert.assertEquals(0, reducer.eventsCount)

        testView?.emit(TestEvent.Ui)
        testView?.emit(TestEvent.Logic)
        testView?.emit(TestEvent.Data(""))

        Assert.assertEquals(3, reducer.eventsCount)
    }

    @Test
    fun testStateChanges() = runTest {
        val flow = testView?.viewModel?.state?.observeState()

        Assert.assertEquals(flow?.firstOrNull()?.state, INITIAL_STATE_VALUE)
        testView?.emit(TestEvent.Data("test"))
        Assert.assertEquals(flow?.firstOrNull()?.state, "test")
    }

    @Test
    fun testStateUnchangedOnLogic() = runTest {
        val flow = testView?.viewModel?.state?.observeState()

        Assert.assertEquals(flow?.firstOrNull()?.state, INITIAL_STATE_VALUE)
        testView?.emit(TestEvent.Logic)
        Assert.assertEquals(flow?.firstOrNull()?.state, INITIAL_STATE_VALUE)
    }
}
