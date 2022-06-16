package ru.surfstudio.mvi.flow.app.test.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule

/** Base class for app unit tests */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseUnitTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    protected val testDispatcher = Dispatchers.Main

    private val dispatchTimeoutMs = 1000L

    /** `runTest` util with defined `dispatchTimeoutMs` for safety */
    protected fun runTimeoutTest(block: suspend () -> Unit) {
        runTest(dispatchTimeoutMs = dispatchTimeoutMs) {
            block()
        }
    }
}