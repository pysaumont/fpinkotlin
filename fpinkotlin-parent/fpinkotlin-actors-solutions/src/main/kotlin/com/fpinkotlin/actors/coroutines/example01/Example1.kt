package com.fpinkotlin.actors.coroutines.example01

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking


fun main(args: Array<String>) {
    exampleA()
    exampleB()
    exampleC()
    exampleD()
    exampleE()
}

fun exampleA() {

    launch { // launch new coroutine in background and continue
        doSuspending()
    }
    print("Hello, ") // main thread continues while coroutine is delayed
    Thread.sleep(2000) // <-- Blocking. Beware if not waiting long enough!
}

private suspend fun doSuspending() {
    delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
    println("World!") // print after delay
}

fun exampleB() {

    launch { // launch new coroutine in background and continue
        doSuspending()
    }
    print("Hello, ") // main thread continues while coroutine is delayed
    runBlocking { // <-- Blocking
        delay(1000) // <-- Non blocking
    }
}

fun exampleC() {

    val lock = Object()

    launch { // launch new coroutine in background and continue
        doSuspending()
        synchronized(lock) {
            lock.notify()
        }
    }
    print("Hello, ") // main thread continues while coroutine is delayed
    synchronized(lock) {
        lock.wait() // <-- Blocking
    }
}

fun exampleD() {

    val job: Job = launch { // launch new coroutine in background and continue
        doSuspending()
    }
    print("Hello, ") // main thread continues while coroutine is delayed
    runBlocking {
        job.join()
    }
}

fun exampleE() = runBlocking {

    val job: Job = launch { // launch new coroutine in background and continue
        doSuspending()
    }
    print("Hello, ") // main thread continues while coroutine is delayed
    job.join()
}

// https://github.com/Kotlin/kotlinx.coroutines/blob/master/coroutines-guide.md#coroutine-basics