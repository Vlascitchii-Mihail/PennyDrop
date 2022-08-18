

import MainPresenter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import MainView
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy

//Позволяет использовать API, обозначенный заданными маркерами
//в аннотированном файле, объявлении или выражении.
@OptIn(ExperimentalCoroutinesApi::class)
class MainViewTest {
    //settings for environment настройки для среды
    //helps to coroutines to start immediately
    private val testCoroutinesDispatcher = StandardTestDispatcher()

    //отслеживает любую корутину
    //lifecycle of coroutine
    //provides all the functions to manage with dispatcher
    private val testCoroutineScope = TestScope(testCoroutinesDispatcher)

    private val testCoroutineContextProvider =
        CoroutineContextProviderImpl(testCoroutinesDispatcher)

    private val mainPresent by lazy { MainPresenter() }
    private val mainView by lazy {
        MainView(mainPresent, testCoroutineContextProvider, testCoroutineScope)
    }

    //runTest - runs the coroutine without delays
    @Test fun testFetchUserData(): Unit = testCoroutineScope.runTest {
        //asserts (утверждает) that an object is null
        assertNull(mainView.userData)

        //creates a new user
        mainView.fetchUserData()

        //Moves the virtual clock of this dispatcher forward
        //skipping the delay from getUser()
        advanceTimeBy(1010)

        // Asserts that two objects are equal
        assertEquals("Filip", mainView.userData?.name)
        mainView.printUserData()
    }

    //runTest - runs the coroutine without delays
    @Test fun exampleTest() = runTest {
        val deferred = async {
            delay(1000)
            async {
                delay(1000)
            }.await()
        }
        //result available immediately
        deferred.await()
    }
}