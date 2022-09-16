import kotlinx.coroutines.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlin.concurrent.thread
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.io.File
import java.io.IOException
import java.lang.ArithmeticException
import java.lang.Exception
import java.lang.IllegalStateException
import java.lang.IndexOutOfBoundsException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import java.util.Observer

/*fun main(args: Array<String>) {
//    (1..1000).forEach {
//        GlobalScope.launch{
//            val threadName = Thread.currentThread().name
//            println("$it printed on thread $threadName")
//        }
//    }
//    Thread.sleep(1000)
//
//    GlobalScope.launch {
//        println("Hello coroutine!")
//        delay(500)
//        println("Right click to ya!")
//    }
//    Thread.sleep(1000)

////Job— это управляющий корутиной элемент . Для каждой создаваемой корутины
//// (с помощью launch или async) он возвращает экземпляр Job, который
//// однозначно идентифицирует корутину и управляет ее жизненным циклом.
//    //CoroutineStart.LAZY - Starts the coroutine lazily, only when it is needed.
//    val job = GlobalScope.launch(start = CoroutineStart.LAZY) {
//        delay(200)
//        println("Pong")
//        delay(200)
//    }
//
////coroutineScope - lifecycle of coroutine
//    GlobalScope.launch {
//        delay(200)
//        println("Ping")
//        job.join()
//        println("Ping")
//        delay(200)
//    }
//    Thread.sleep(1000)


//    with(GlobalScope) {
//        val parentJob = launch {
//            delay(200)
//            println("I'm the parent")
//            delay(200)
//        }
//
//        //child to the parentJob, runs first
//        launch(context = parentJob) {
//            delay(200)
//            println("I'm a child")
//            delay(200)
//        }
//
//        if (parentJob.children.iterator().hasNext()) {
//            println("The job has children!")
//        } else println("The Job has NOT children")
//    }
//    Thread.sleep(1000)



//    var isDoorOpen = false
//    println("Unlocking the door....please wait!\n")
//
//    GlobalScope.launch {
//        delay(3000)
//        isDoorOpen = true
//    }
//
//    GlobalScope.launch {
//        repeat(4) {
//            println("Trying to open the door....")
//            delay(800)
//
//            if (isDoorOpen) println("The door opened\n")
//            else println("The door is  still locked\n")
//        }
//    }
//    Thread.sleep(5000)


//    GlobalScope.launch {
//        val bgThreadName = Thread.currentThread().name
//        println("I'm Job 1 in thread $bgThreadName")
//        delay(200)
//
//        //send the data to the main - UI thread
//        GlobalScope.launch(Dispatchers.Main) {
//            val uiThreadName = Thread.currentThread().name
//            println("I'm Job 2 in thread $uiThreadName")
//        }
//    }
//    Thread.sleep(2000)
}*/

//class User(val ID: String, name: String="") {
//    companion object {
//        fun getUserStandard(userId: String): User {
//
//            //send the UI thread to sleep mode
//            Thread.sleep(2000)
//            return User(userId)
//        }
//
//        //send the UI thread to sleep mode using callbacks
//        fun getUserNetworkCallback(userId: String, onUserReady: (User?, Throwable?) -> Unit) {
//
////            Creates a thread that runs the specified block of code.
//            thread {
//                try {
//                    Thread.sleep(1000)
//                    val user = User(userId, "Filip")
//                    onUserReady(user, null)
//                } catch (error: Throwable) {
//                    onUserReady(null, error)
//                }
//            }
//            println("end")
//        }
//    }
//}
//
//suspend fun getUserSuspend(userId: String): User {
//    delay(1000)
//    return User(userId, "Filip")
//}
//
//suspend fun getUserSuspend2(userId: String): User =
//
////creates new user in background thread
//    //withContext() - change context, change Thread
//    withContext(Dispatchers.Default) {
//        delay(1000)
//        User(userId, "Filip")
//    }
//
//
//fun main() {
//    println(User.getUserStandard("Filip").ID)
//    User.getUserNetworkCallback("123") { user, error ->
//        user?.let { println(it.ID) }
//    error?.printStackTrace()}
//    println("Main thread")
//
//    GlobalScope.launch {
//        val user = getUserSuspend("222")
//        println(user.ID)
//    }
//    Thread.sleep(1500)
//
//    GlobalScope.launch(Dispatchers.Main) {
//        val user = getUserSuspend2("202")
//        println(user.ID)
//    }
//}

data class User(val ID: Int, val name: String, val lastName: String)

private fun getUserByIdFromNetwork(userId: Int, parentScope: CoroutineScope) =
    //creates coroutine and returns its future result
    parentScope.async {
        if (!isActive) return@async User(0, "", "")
        println("scope is active")

        println("Retrieving user from network")
        delay(3000)
        println("Still in the coroutine")
        User(userId, "Filip", "Babic")
    }

private fun readUsersFromFile(filePath: String, parentScope: CoroutineScope) =
    parentScope.async {
        println("Reading the file of users")
        delay(1000)
        println("Reading after delay")

        //File(filePath).readLines().asSequence() - reads a txt file
        File(filePath).readLines().asSequence()
            .filter { it.isNotEmpty() }.map {
                val data = it.split(" ")
                if(data.size == 3) data else emptyList()
            }
            .filter {
                it.isNotEmpty()
            }
            .map {
                val userId = it[0].toInt()
                val name = it[1]
                val lastName = it[2]
                User(userId, name, lastName)
            }
            .toList()
    }

//fun <T> produceValue(scope: CoroutineScope): Deferred<T> {
//    scope.async {
//        var data: T? = null
//
//        requestData<T> { value ->
//            data = value
//        }
//
//        while (data == null && scope.isActive) {
//
//        }
//
//        data!!
//    }
//}

private fun checkUserExists(user: User, users: List<User>): Boolean {
    return user in users
}

//creates custom coroutine scope
class CustomScope: CoroutineScope {
    private var parentJob = Job()

//CoroutineContext— это набор элементов, определяющих поведение корутины.
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + parentJob

    //functions of lifecycle of CoroutineScope
    fun onStart() {
        parentJob = Job()
    }

    fun onStop() {
        parentJob.cancel()
    }
}
//
//interface CoroutineContextProvider {
//    fun context(): CoroutineContext
//}
//
//class CoroutineContextProviderImpl(private val context: CoroutineContext): CoroutineContextProvider {
//        override fun context(): CoroutineContext = context
//    }



fun main() {
//    val userId = 992
//    GlobalScope.launch {
//        val userData = getUserByIdFromNetwork(userId)
//        println(userData.await())
//    }
//    Thread.sleep(5000)


//    val userId = 992
//    val launch = GlobalScope.launch {
//        delay(1000)
//        val userDeferred = getUserByIdFromNetwork(userId, GlobalScope)
//        println("Not canceled")
//        val userFromFileDeferred = readUsersFromFile("Users.txt", GlobalScope)
//
//        //call checkUserExists() if userDeferred.await() and userFromFileDeferred.await() are executed
//        val userStoredInFile = checkUserExists(userDeferred.await(), userFromFileDeferred.await())
//
//        if (userStoredInFile) println("Found user in file")
//    }
//    launch.cancel()
//    Thread.sleep(6000)



    val userId = 992
    val scope = CustomScope()
    scope.launch {
//        delay(2000)
        println("Launching in custom scope for finding user")

//    scope.onStop()
        //userDeferred keeps reference to the async coroutine in function
        val userDeferred = getUserByIdFromNetwork(userId, scope)
        //userFromFileDeferred keeps reference to the async coroutine in function
        val userFromFileDeferred = readUsersFromFile("Users.txt", scope)
        //await() waits finishing of the function
        val userStoredInFile = checkUserExists(userDeferred.await(), userFromFileDeferred.await())

        if (userStoredInFile) println("Found user in file")
//        scope.onStop()
    }
    //stops the coroutine scope using Job() and coroutines don't continue running
    scope.onStop()




//    val scope = CustomScope()
//    val defaultDispatcher = Dispatchers.Default
//    val coroutineErrorHandler = CoroutineExceptionHandler{
//        context, error -> println("The problem with Coroutine: $error")
//    }
//
//    val emptyJob = Job()
//    val combineContext = defaultDispatcher + coroutineErrorHandler + emptyJob
//
//    scope.launch(context = combineContext) {
//        println(Thread.currentThread().name)
//    }
//    Thread.sleep(50)


//    val parentJob = Job()
//    val provider: CoroutineContextProvider =
//        CoroutineContextProviderImpl(context = parentJob + Dispatchers.IO)
//
//    GlobalScope.launch(context = provider.context()) {
//        println(Thread.currentThread().name)
//    }
//    Thread.sleep(50)
//
//
//    GlobalScope.launch(context = Dispatchers.Default) {
//        println(Thread.currentThread().name)
//    }
//    Thread.sleep(50)
//
//    val executorDispatcher = Executors.newWorkStealingPool().asCoroutineDispatcher()
//
//    GlobalScope.launch(context = executorDispatcher) {
//        println(Thread.currentThread().name)
//    }
//    Thread.sleep(50)
//
}

//fun main() {
//    val handler = CoroutineExceptionHandler {
//            context, exception -> println("Caught $exception")
//    }
//
//    val scope = CoroutineScope(Job())
//    scope.launch(handler) {
//        launch {
//            throw Exception("Failed coroutine")
//        }
//    }
//}

//fun main() = runBlocking {
//    val launchJob = GlobalScope.launch {
//        println("1. Exception created via launch coroutine")
//        throw IndexOutOfBoundsException()
//    }
//
//    launchJob.join()
//    println("2. Joined failed job")
//
//    val deferred = GlobalScope.async {
//        println("3. Exception created via async coroutine")
//        throw ArithmeticException()
//    }
//
//    try {
//        deferred.await()
//        println("4. Unreachable, this statement is never executed")
//    } catch (e: Exception) {
//        println("5. Caught ${e.javaClass.simpleName}")
//    }
//}


/*fun main() {
    runBlocking {

//        val exceptionHandler = CoroutineExceptionHandler {
//            _, exception -> println("Caught $exception")
//    }
//
//        val job = GlobalScope.launch(exceptionHandler) {
//            throw AssertionError("My custom assertion error!")
//        }
//
//        val deferred = GlobalScope.async(exceptionHandler) {
//            println("Hello")
//            throw AssertionError("Exception from async")
//        }
//
//        joinAll(job, deferred)



//        val callAwaitDeferred = true
//
//        val deferred = GlobalScope.async(exceptionHandler) {
//            println("Throwing exception from async")
//            throw ArithmeticException("Something crashed")
//        }
//
//        if (callAwaitDeferred) {
//            try{
//                deferred.await()
//            } catch (e: ArithmeticException) {
//                println("Caught ArithmeticException")
//            }
//        }


//        val handler = CoroutineExceptionHandler {_, exception ->
//            println("Caught $exception with suppressed " +
//                    "${exception.suppressed?.contentToString()}")
//        }
////
//        val parentJob = GlobalScope.launch(handler) {
//            //child 1
//            launch {
//                try {
//                    delay(Long.MAX_VALUE)
//                } catch (e: Exception) {
//                    println("${e.javaClass.simpleName} in Child job 1")
//                } finally {
//                    println("From final")
//                    throw ArithmeticException()
//                }
//            }
//
//            launch {
//                delay(100)
//                throw IllegalStateException()
//            }
//
//            delay(Long.MAX_VALUE)
//        }
//        parentJob.join()

//        val scope = CoroutineScope(Job())
//        scope.launch {
//            launch(handler) {
//                    throw ArithmeticException()
//            }
//        }

//        val scope = CoroutineScope(Job())
//        scope.launch(handler) {
//            launch {
//                throw Exception("Failed coroutine")
//            }
//        }
//        Thread.sleep(200)


//        try {
//            val data = getDataAsync()
//            println("Data received: $data")
//        } catch (e: Exception) {
//            println("Caught ${e.javaClass.simpleName}")
//        }
//
    }
}*/

// Method to simulate a long running task
//fun getData(asyncCallback: AsyncCallback) {
//    val triggerError = false
//
//    try {
//        Thread.sleep(3000)
//        if (triggerError) {
//            throw IOException()
//        } else {
//            // Send success
//            asyncCallback.onSuccess("[Beep.Boop.Beep]")
//        }
//    } catch (e: Exception) {
//        // send error
//        asyncCallback.onError(e)
//    }
//}
//
//// Callback
//interface AsyncCallback {
//    fun onSuccess(result: String)
//    fun onError(e: Exception)
//}
//
//
//@OptIn(ExperimentalCoroutinesApi::class)
//suspend fun getDataAsync(): String {
//    return suspendCancellableCoroutine { continuation ->
//        getData(object : AsyncCallback {
//            override fun onSuccess(result: String) {
//                continuation.resume(result)
//            }
//
//            override fun onError(e: Exception) {
//                continuation.resumeWithException(e)
//            }
//        })
//    }
//}


/*fun main() = runBlocking {
//    val supervisor = SupervisorJob()
    //don't let to exceptions to go upper
//    with(CoroutineScope(coroutineContext + supervisor)) {
//        val firstChild = launch {
//            println("FirstChild throwing an exception")
//            throw ArithmeticException()
//        }
//
//        val secondChild = launch {
//            println("FirstChild is canceled: ${firstChild.isCancelled}")
//            try {
//                delay(5000)
//            } catch (e: CancellationException) {
//                println("Second child cancelled because supervisor got cancelled.")
//            }
//        }
//
//        firstChild.join()
//        println("Second child is active: ${secondChild.isActive}")
//        supervisor.cancel()
//        secondChild.join()
//    }
//}

//don't let to exceptions to go upper
//    supervisorScope {
//        val result = async {
//            println("Throwing exception in async")
//            throw IllegalStateException()
//        }
//
//        try {
//            result.await()
//        } catch (e: Exception) {
//            println("Caught $e")
//        }
//    }



//    val startTime = System.currentTimeMillis()
//    val job = launch(Dispatchers.Default) {
//        var nextPrintTime = startTime
//        var i = 0
    //checking if the coroutine was cancelled
//        while (i < 10 && isActive) {
//            if (System.currentTimeMillis() >= nextPrintTime) {
//                println("Doing heavy work: $i")
//                i++
//                nextPrintTime += 500L
//            }
//        }
//    }
//    delay(1000)
//    println("Cancelling coroutine")
//    job.cancel()
//    println("Main: now i can quit!")




//    val startTime = System.currentTimeMillis()
//    val job = launch(Dispatchers.Default) {
//        var i = 0
//        while (i < 1000) {
//            println("Doing heavy work: ${i++}")

    //checking if the coroutine was cancelled
//            delay(500)
//        }
//    }
//    delay(1000)
//    println("Cancelling coroutine")
//    job.cancel()
//    println("Main: now i can quit!")


////catching Exception e
//    val handler = CoroutineExceptionHandler { _, exception ->
//        println("Caught original $exception")
//    }
//
//    val parentJob = GlobalScope.launch(handler) {
//        val childJob = launch {
//            throw IOException()
//        }
//
//        try {
//            //waiting for completion of the child job
//            //can't catch IOException
//            childJob.join()
//        } catch (e: CancellationException) {
//            println("Rethrowing CancellationException with original" +
//                    " cause: ${e.cause}")
//            throw e
//        }
//    }
//    parentJob.join()


//    val job = launch {
//        println("Crunching numbers [Beep.Beep]")
//        delay(1000)
//    }
//    job.join()
//    println("main: Now i can quit")



//        val jobOne = launch {
//            println("Job 1: Crunching numbers [Beep.Boop.Beep]…")
//            delay(2000L)
//        }
//        val jobTwo = launch {
//            println("Job 2: Crunching numbers [Beep.Boop.Beep]…")
//            delay(500L)
//        }
//        // waits for both of the jobs to complete
//        joinAll(jobOne, jobTwo)
//        println("main: Now I can quit.")



//    val job = launch {
//        repeat(1000) { i ->
//            println("$i repeats")
//            delay(500)
//        }
//    }
//
//    delay(1300)
//    println("main: I'm tired of waiting!")
//
//    //cancelling the job and waits for completing a current task
//    job.cancelAndJoin()
//    println("main: Now I can quit")
}*/


//fun main() = runBlocking {
//    val parentJob = launch {
//        val childOne = launch {
//            repeat(1000) { i ->
//                println("ChildOne coroutine: $i")
//                delay(500L)
//            }
//        }
//
//        //invoked once on completion of this job
//        childOne.invokeOnCompletion { exception ->
//            println("ChildJob: ${exception?.message}")
//        }
//
//        val childTwo = launch {
//            repeat(1000) { i ->
//                println("ChildTwo coroutine: $i")
//                delay(500L)
//            }
//        }
//
//        //invoked once on completion of this job
//        childTwo.invokeOnCompletion { exception ->
//            println("ChildTwo: ${exception?.message}")
//        }
//    }
//    delay(1200L)
//    println("Calling cancelChildren() on the parentJob")
//
//    //cancel all child coroutines
//    parentJob.cancelChildren()
//    println("parentJob isActive: ${parentJob.isActive}")
//}


//fun main() = runBlocking {
//    try {
//
//        //runs suspending block inside a coroutine with timeout
//        //throws a TimeoutCancellationException
//        withTimeout(1500L) {
//            repeat(1000) { i ->
//                println("$i  attempts")
//                delay(500)
//            }
//        }
//    } catch (e: TimeoutCancellationException) {
//        println("caught ${e.javaClass.simpleName}")
//    }
//}


//fun main() = runBlocking {
//
//    //runs suspending block inside a coroutine with timeout
//    //returns NULL if timeout was exceeded(превышено)
//    val result = withTimeoutOrNull(1300L) {
//        repeat(2) { i->
//            println("$i attempts")
//            delay(500L)
//        }
//        "Done"
//    }
//    println("Result is $result")
//}

////returns: filter, map, forEach, filter, map, forEach, filter, map, forEach,
//fun main() {
//    val list = listOf(1, 2, 3)
//
//    //отправляет по одному элементу через все функции
//    list.asSequence().filter {
//        println("filter, ")
//        it > 0
//    }.map {
//        println("map, ")
//        it.toString()
//    }.forEach {
//        println("forEach, $it")
//    }
//}



//var NUM = 0
//
//fun main() {
//
//    //take(n) - Возвращает последовательность, содержащую первые n элементов.
//    var sequence = generatorFib().take(8)
//    sequence.forEach {
//        println("$it $NUM")
//    }
//}
//
//fun generatorFib() = sequence {
//    print("Suspending out...")
//
//    //Выдает значение создаваемому итератору и приостанавливает
//    // работу до тех пор, пока не будет запрошено следующее значение.
//    yield(0L)
//    var cur = 0L
//    var next = 1L
//    while (true) {
//        print("Suspending in while...")
//        NUM++
//
//        //Выдает значение создаваемому итератору и приостанавливает
//        // работу до тех пор, пока не будет запрошено следующее значение.
//        yield(next)
//        var temp = cur + next
//        cur = next
//        next = temp
//    }
//}


//fun main() {
//    val sequence = singleValueExample()
//
//    //printing yielded element
//    sequence.forEach {
//        println(it)
//    }
//}
//
//fun singleValueExample() = sequence {
//    println("Printing first value")
//
//    //Выдает значение создаваемому итератору и приостанавливает
//    // работу до тех пор, пока не будет запрошено следующее значение.
//    yield("Apple")
//
//    println("Printing second value")
//    yield("Orange")
//
//    println("Printing third value")
//    println("Banana")
//}


//fun main() {
//    val sequence = iterableExample()
//
//    //printing yielded collection
//    sequence.forEach {
//        println("$it")
//    }
//}
//
//fun iterableExample() = sequence {
//
//    //Выдает коллекции значений для создаваемого Iterator и
//    // приостанавливает работу до тех пор, пока все эти
//    // значения не будут итерированы и не будет запрошено следующее.
//    yieldAll(1..5)
//}


//fun main() {
//    val sequence = sequenceExample().take(10)
//    sequence.forEach {
//        print("$it ")
//    }
//}
//
//fun sequenceExample() = sequence {
//
//    //Выдает потенциально бесконечную последовательность
//    // значений для создаваемого Iterator и приостанавливает
//    // работу до тех пор, пока все эти значения не будут
//    // итерированы и не будет запрошено следующее.
//
//    //yields all generated sequence
//    //seed - start value of sequence
//    yieldAll(generateSequence(2) {it * 2})
//}


//var NUM = 0
//fun main() {
//
//    //produces data for listeners
//    //creating cold thread
//    val flowOfStrings = flow {
//        for (number in 1 .. 100) {
//
//            //выдает данные слушателю
//            emit("Emitting: $number")
//            NUM++
//        }
//    }
//
//    GlobalScope.launch {
////        flowOfStrings.collect {value ->
////            println("$value  $NUM")
////        }
//
//        flowOfStrings
//            .map {it.split(" ") }
//            .map { it.last() }
//
//                //Возвращает поток, который вызывает данное действие
//            // до того, как каждое значение восходящего потока будет
//            // передано нисходящему потоку.
//            .onEach { delay(100) }
//
//                //Изменяет контекст, в котором выполняется этот поток,
//            // на заданный контекст. Этот оператор составной и влияет только
//            // на предшествующие операторы, не имеющие собственного контекста.
//            // Этот оператор сохраняет контекст: контекст не просачивается
//            // в нижестоящий поток.
//            .flowOn(Dispatchers.Default)
//
//                //listener of flowOfStrings
//            .collect { value ->
//                println(value)
//            }
//    }
//
//    Thread.sleep(1000)
//}


//fun main() {
//
//    //Создает экземпляр холодного потока с элементами, которые отправляются
//    // в SendChannel, предоставляемый блоку кода построителя через
//    // ProducerScope. Это позволяет создавать элементы с помощью кода,
//    // который выполняется в другом контексте или одновременно.
//    // Результирующий поток является холодным, что означает, что блок
//    // вызывается каждый раз, когда оператор терминала применяется
//    // к результирующему потоку.
//    val flowOfStrings = channelFlow {
//        for (number in 1 .. 100) {
//
//            //Вызывает указанный блок приостановки с заданным контекстом
//            //сопрограммы, приостанавливает до завершения и возвращает результат.
//            withContext(Dispatchers.Default) {
//
//                //Немедленно добавляет указанный элемент в этот канал,
//                // если это не нарушает его ограничения емкости, и
//                // возвращает успешный результат. В противном случае
//                // возвращает неудачный или закрытый результат. Это
//                // синхронный вариант отправки, который отключается в
//                // ситуациях, когда отправка приостанавливается или
//                // выбрасывается.
//                trySend("Emitting: $number")
//            }
//        }
//    }
//
//    GlobalScope.launch {
//
//        //flowOfStrings listener
//        flowOfStrings.collect {
//            println(it)
//        }
//    }
//
//    Thread.sleep(1000)
//}


//var NUM = 0
//fun main() {
//    val flowOfStrings = flow {
//        for (number in 1 .. 100) {
//            emit("")
//            NUM++
//        }
//    }
//
//    GlobalScope.launch {
////        flowOfStrings.collect {value ->
////            println("$value  $NUM")
////        }
//
//        flowOfStrings
//            .map {it.split(" ") }
//            .map { it[1] }
////            .onEach { delay(100) }
//            .catch {
////                it.printStackTrace()
//                emit("Fallback")
//            }
//            .flowOn(Dispatchers.Default)
//            .collect { value ->
//                println(value)
//            }
//
//        println("The code still works")
//    }
//
//    Thread.sleep(1000)
//}

//Kotlin Coroutines by Tutorials Chapter 12: SharedFlow & StateFlow

//fun main() {
//    //reply 2 - buffer for 2 values, hot stream
//    val sharedFlow = MutableSharedFlow<Int>(2)
//
//    //onEach - creates a new subscriber and starts listens to the stream
//    sharedFlow.onEach {
//        println("Emitting: $it")
//        //subscribing to event and starts listening to the following stream in CoroutineScope
//    }.launchIn(GlobalScope)
//
//    sharedFlow.onEach {
//        println("Hello: $it")
//    }.launchIn(GlobalScope)
//
//    //sends the following values to the sharedFlow uses different streams
//    sharedFlow.tryEmit(5)
//    sharedFlow.tryEmit(3)
//
//    Thread.sleep(50)
//}


//fun main() {
//    //reply 2 - buffer for 2 values, hot stream
//    val sharedFlow = MutableSharedFlow<Int>(2)
//
//    //sends the following values to the sharedFlow uses different streams
//    sharedFlow.tryEmit(5)
//    sharedFlow.tryEmit(3)
//    sharedFlow.tryEmit(1)
//
//    //onEach - creates a new subscriber and starts listens to the stream
//    sharedFlow.onEach {
//        println("Emitting: $it")
//        //subscribing to event and starts listening to the following stream in CoroutineScope
//    }.launchIn(GlobalScope)
//
//    sharedFlow.onEach {
//        println("Hello: $it")
//    }.launchIn(GlobalScope)
//
//    Thread.sleep(50)
//}


//fun main() {
//    //creating own coroutine scope
//    val coroutineScope = CoroutineScope(Dispatchers.Default)
//    //reply 2 - buffer for 2 values, hot stream
//    val sharedFlow = MutableSharedFlow<Int>(2)
//
//    //onEach - creates a new subscriber and starts listens to the stream
//    sharedFlow.onEach {
//        println("Emitting: $it")
//        //subscribing to event and starts listening to the following stream in CoroutineScope
//    }.launchIn(coroutineScope)
//
////    sharedFlow.onEach {
////        println("Hello: $it")
////    }.launchIn(GlobalScope)
//
//    coroutineScope.launch {
//        //sends the following values to the sharedFlow uses different streams
//        sharedFlow.emit(5)
//        sharedFlow.emit(3)
//        sharedFlow.emit(1)
//
//        coroutineScope.cancel()
//    }
//
//    while (coroutineScope.isActive) {
////        println("isActive")
//    }
//}


//fun main() {
//    val coroutineScope = CoroutineScope(Dispatchers.Default)
//    //Creates a cold flow
//    val sharedFlow = flow {
//        //sends values
//        emit(5)
//        emit(3)
//        emit(1)
//
//        Thread.sleep(50)
//        coroutineScope.cancel()
//        //converting the Flow to a SharedFlow
//        //SharingStarted.Lazily - waits his first subscriber and after this sends value
//        //Eagerly - starts immediately
//    }.shareIn(coroutineScope, started = SharingStarted.Lazily)
//
//    sharedFlow.onEach {
//        println("Emitting: $it")
//    }.launchIn(coroutineScope)
//
//    while (coroutineScope.isActive) {
//
//    }
//}


//fun main() {
//    val coroutineScope = CoroutineScope(Dispatchers.Default)
//    val stateFlow = MutableStateFlow("Author: Filip")
//
//    //access to stateFlow's value without subscribing
//    println(stateFlow.value)
//
//    //subscribing to changing the value
//    coroutineScope.launch {
//        stateFlow.collect {
//            println(it)
//        }
//    }
//
//    //changing stateFlow.value 3 ways
//    stateFlow.value = "Author: Luca"
//
//    stateFlow.tryEmit("FRE: MAX")
//
//    //top priority
//    coroutineScope.launch {
//        stateFlow.emit("TE: Godfrey")
//    }
//
//
//    Thread.sleep(50)
//    coroutineScope.cancel()
//
//    while(coroutineScope.isActive) {
//
//    }
//}


//Chapter 13: Testing Coroutines

//interface CoroutineContextProvider {
//    fun context(): CoroutineContext
//}
//
////creating coroutine context
//class CoroutineContextProviderImpl(private val context: CoroutineContext)
//    : CoroutineContextProvider {
//
//    override fun context(): CoroutineContext = context
//    }
//
////model's class
//data class User(val id: String, val name: String)
//
////creating new user
//class MainPresenter {
//    suspend fun getUser(userId: String): User {
//        delay(1000)
//        return User(userId, "Filip")
//    }
//}
//
//class MainView(private val presenter: MainPresenter,
//    private val contextProvider: CoroutineContextProvider,
//    private val coroutineScope: CoroutineScope) {
//    var userData: User? = null
//
//    //launching suspend function
//    fun fetchUserData() {
//        coroutineScope.launch(contextProvider.context()) {
//            userData = presenter.getUser("101")
//        }
//    }
//
//    fun printUserData() {
//        println(userData)
//    }
//}


////creation new background Thread
//class MyThread: Thread() {
//    override fun run() {
//        println("Background Thread")
//    }
//}
//
//fun main() {
//    val newThread = MyThread()
//    newThread.start()
//    println(newThread.name)
//}


////implementation of the interface Runnable
//class MyRunnable: Runnable {
//    override fun run() {
//        println("Background Thread")
//    }
//}
//
//fun main() {
//    val runnable = MyRunnable()
//
//    //transmitting Runnable in Thread()'s constructor
//    val thread = Thread(runnable)
//    thread.start()
//}
