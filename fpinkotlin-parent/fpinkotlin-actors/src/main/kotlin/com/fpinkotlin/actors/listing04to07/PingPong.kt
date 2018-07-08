package com.fpinkotlin.actors.listing04to07

import com.fpinkotlin.actors.listing01to03.AbstractActor
import com.fpinkotlin.actors.listing01to03.Actor
import com.fpinkotlin.common.Result
import java.util.concurrent.Semaphore


private class Player(id: String,
             private val sound: String,
             private val referee: Actor<Int>) : AbstractActor<Int>(id) {

    override fun onReceive(message: Int, sender: Result<Actor<Int>>) {
        println("$sound - $message")
        if (message >= 10) {
            referee.tell(message, sender)
        } else {
            sender.forEach(
                { actor: Actor<Int> -> actor.tell(message + 1, self())},
                { referee.tell(message, sender) }
            )
        }
    }
}

fun player(id: String,
           sound: String,
           referee: Actor<Int>) = object : AbstractActor<Int>("id") {

    override fun onReceive(message: Int, sender: Result<Actor<Int>>) {
        println("$sound - $message")
        if (message >= 10) {
            referee.tell(message, sender)
        } else {
            sender.forEach(
                { actor: Actor<Int> -> actor.tell(message + 1, self())},
                { referee.tell(message, sender) }
                          )
        }
    }
}

private val semaphore = Semaphore(1)

fun main(args: Array<String>) {
    val referee = object : AbstractActor<Int>("Referee") {

        override fun onReceive(message: Int, sender: Result<Actor<Int>>) {
            println("Game ended after $message shots")
            semaphore.release()
        }
    }

    val player1 = player("Player1", "Ping", referee)
    val player2 = player("Player2", "Pong", referee)

    semaphore.acquire()
    player1.tell(1, Result(player2))
    semaphore.acquire()
}
