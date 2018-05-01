package com.fpinkotlin.actors.coroutines.example25

import kotlinx.coroutines.experimental.cancelChildren
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking


data class Ball(val hits: Int = 0) {
    fun rethrow() = Ball(hits + 1)
}

fun main(args: Array<String>) = runBlocking<Unit> {
    val table = Channel<Ball>() // a shared table
    val referee = Channel<Ball>()
    launch(coroutineContext) { player("ping", table, referee) }
    launch(coroutineContext) { player("pong", table, referee) }
    table.send(Ball()) // serve the ball
    referee.receive()
    coroutineContext.cancelChildren() // game over, cancel them
}

suspend fun player(name: String, table: Channel<Ball>, referee: Channel<Ball>) {
    for (ball in table) { // receive the ball in a loop
        println("$name $ball")
        delay(300) // wait a bit
        when (ball.hits) {
            10 -> referee.send(ball)
            else -> table.send(ball.rethrow()) // send the ball back
        }
    }
}