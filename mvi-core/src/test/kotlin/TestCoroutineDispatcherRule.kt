import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * A test rule that sets the Main coroutine dispatcher for unit testing.
 */
@ExperimentalCoroutinesApi
class TestCoroutineDispatcherRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher(),
    val testScope: TestScope = TestScope(testDispatcher),
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
