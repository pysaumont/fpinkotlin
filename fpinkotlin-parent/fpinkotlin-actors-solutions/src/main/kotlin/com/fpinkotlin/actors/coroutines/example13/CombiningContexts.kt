package com.fpinkotlin.actors.coroutines.example13

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking


fun main(args: Array<String>) = runBlocking {
    // start a coroutine to process some kind of incoming request
    val request = launch(coroutineContext) { // use the context of `runBlocking`
        // spawns CPU-intensive child job in CommonPool !!!
        val job1 = launch(coroutineContext + CommonPool) {
            println("job1: I am a child of the request coroutine, but with a different dispatcher")
            delay(1000)
            println("job1: I will not execute this line if my parent request is cancelled")
        }
        val job2 = launch(CommonPool) {
            println("job2: I am a child of the request coroutine, but with a different dispatcher")
            delay(1000)
            println("job2: I will not execute this line if my parent request is cancelled")
        }
        job1.join() // request completes when its sub-job completes
        job2.join() // request completes when its sub-job completes
    }
    delay(500)
    request.cancel() // cancel processing of the request
    delay(1000) // delay a second to see what happens
    println("main: Who has survived request cancellation?")
}