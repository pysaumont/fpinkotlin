package com.fpinkotlin.actors.coroutines.example06

import com.fpinkotlin.common.Result
import kotlinx.coroutines.experimental.CancellationException
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withTimeout


fun main(args: Array<String>) {
    val message: Result<String> = try {
        runBlocking {
            withTimeout(1300L) {
                repeat(1000) { i ->
                    println("I'm sleeping $i ...")
                    delay(500L)
                }
            }
            Result("Done !")
        }
    } catch (e: CancellationException) {
        Result.failure(e.message ?: "No message")
    }
    println(message)
}