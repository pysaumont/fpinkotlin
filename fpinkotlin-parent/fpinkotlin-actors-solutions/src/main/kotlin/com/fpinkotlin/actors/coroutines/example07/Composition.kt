package com.fpinkotlin.actors.coroutines.example07

import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import kotlin.system.measureTimeMillis


suspend fun doSomethingUsefulOne(): Int {
    delay(1000L) // pretend we are doing something useful here
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000L) // pretend we are doing something useful here, too
    return 29
}

// The result type of somethingUsefulOneAsync is Deferred<Int>
fun somethingUsefulOneAsync(): Deferred<Int> = async {
    doSomethingUsefulOne()
}

// The result type of somethingUsefulTwoAsync is Deferred<Int>
fun somethingUsefulTwoAsync(): Deferred<Int> = async {
    doSomethingUsefulTwo()
}

fun main(args: Array<String>) = runBlocking {
    val time = measureTimeMillis {
        val one = doSomethingUsefulOne()
        val two = doSomethingUsefulTwo()
        println("The answer is ${one + two}")
    }
    println("Completed in $time ms")
    val time2 = measureTimeMillis {
        val one: Deferred<Int> = async { doSomethingUsefulOne() }
        val two: Deferred<Int> = async { doSomethingUsefulTwo() }
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time2 ms")
    val time3 = measureTimeMillis {
        val one: Deferred<Int> = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
        val two: Deferred<Int> = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time3 ms")
    val time4 = measureTimeMillis {
        val one: Deferred<Int> = somethingUsefulOneAsync()
        val two: Deferred<Int> = somethingUsefulTwoAsync()
        runBlocking {
            println("The answer is ${one.await() + two.await()}")
        }
    }
    println("Completed in $time4 ms")
}