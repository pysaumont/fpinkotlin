package com.fpinkotlin.actors.coroutines.example05

import com.fpinkotlin.common.range
import kotlinx.coroutines.experimental.JobCancellationException
import kotlinx.coroutines.experimental.NonCancellable
import kotlinx.coroutines.experimental.cancelAndJoin
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withContext


fun main(args: Array<String>) = runBlocking {
    val job = launch {
        try {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        } catch (e: JobCancellationException) {
            println("JobCancellationException: ${e.message}")
        }
        finally {
            // delay(1000) // Using a suspending function will throw an exception, thus cancelling the finally block.
            withContext(NonCancellable) { // Wrap in withContext(NonCancellable) to use a suspending function
                println("I'm running finally")
                delay(1000L)
                println("And I've just delayed for 1 sec because I'm non-cancellable")
            }

            range(0, 500).forEach { println("$it: ${slowFibonacci(it % 40)}") }
            println("I'm running finally")
        }
    }
    // delay(1300L) // No need to delay
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels will wait for completion of "finally" only if it does not include delay
    println("main: Now I can quit.")
}

private fun slowFibonacci(number: Int): Int {
    return when (number) {
        0    -> 1
        1    -> 1
        else -> slowFibonacci(number - 1) + slowFibonacci(number - 2)
    }
}
