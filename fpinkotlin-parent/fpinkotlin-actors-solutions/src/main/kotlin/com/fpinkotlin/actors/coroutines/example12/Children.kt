package com.fpinkotlin.actors.coroutines.example12

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

fun main(args: Array<String>) = runBlocking {
    // launch a coroutine to process some kind of incoming request
    log("I am the main coroutine")
    val request = launch {
        // it spawns two other jobs, one with its separate context
        val job1 = launch {
            log("job1: I have my own context and execute independently!")
            delay(1000)
            log("job1: I am not affected by cancellation of the request")
        }
        // and the other inherits the parent context
        val job2 = launch(coroutineContext) {
            delay(100)
            log("job2: I am a child of the request coroutine")
            delay(1000)
            log("job2: I will not execute this line if my parent request is cancelled")
        }
        // request completes when both its sub-jobs complete:
        job1.join()
        job2.join()
    }
    delay(500)
    request.cancel() // cancel processing of the request
    delay(1000) // delay a second to see what happens
    log("main: Who has survived request cancellation?")
}