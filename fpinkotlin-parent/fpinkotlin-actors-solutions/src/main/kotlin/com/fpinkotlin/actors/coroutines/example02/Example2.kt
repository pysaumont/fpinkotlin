package com.fpinkotlin.actors.coroutines.example02

import com.fpinkotlin.common.range
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


//fun main(args: Array<String>) {

//    val task = launch { // launch new coroutine in background and continue
//        delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
//        println("World!") // print after delay
//    }
//    print("Hello, ") // main thread continues while coroutine is delayed
//    task.join()
//}
// https://github.com/Kotlin/kotlinx.coroutines/blob/master/coroutines-guide.md#coroutine-basics

fun main(args: Array<String>) = threads()

private fun threads() {
    val executor: ExecutorService = Executors.newFixedThreadPool(2000)
    range(0, 100_000).forEach { executor.submit(::doJob)}
    executor.shutdown()
}

private fun coroutines() {
    runBlocking {
        val jobs = List(100_000) {
            launch {
                doJobSuspending()
            }
        }
        jobs.forEach {
            it.join()
        }
    }
}

var x = 0

private suspend fun doJobSuspending() {
    delay(1000L)
    println(x++)
}

private fun doJob() {
    Thread.sleep(1000L)
    println(x++)
}