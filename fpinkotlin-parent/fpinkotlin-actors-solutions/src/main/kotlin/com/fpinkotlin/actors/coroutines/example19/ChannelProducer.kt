package com.fpinkotlin.actors.coroutines.example19

import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.runBlocking


fun produceSquares() = produce {
    for (x in 1..5) send(x * x)
}

fun main(args: Array<String>) = runBlocking {
    val squares = produceSquares()
    squares.consumeEach { println(it) }
    println("Done!")
}