package com.fpinkotlin.actors.coroutines.example03

import kotlinx.coroutines.experimental.cancelAndJoin
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking


fun main(args: Array<String>) {
    val result = runBlocking {
        val job = launch {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }
        delay(1300L) // just quit after delay
        println("main: I'm tired of waiting!")
        job.cancelAndJoin()
        "main: Now I can quit."
    }
    println(result)
}