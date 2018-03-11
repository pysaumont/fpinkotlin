package com.fpinkotlin.actors.listing04_05

import com.fpinkotlin.actors.listing01_02_03.AbstractActor
import com.fpinkotlin.actors.listing01_02_03.Actor
import com.fpinkotlin.common.Result
import java.util.concurrent.Semaphore


private class Player(id: String,
             private val sound: String, // <1>
             private val referee: Actor<Int>) : AbstractActor<Int>(id) { // <2>

    override fun onReceive(message: Int, sender: Result<Actor<Int>>) {
        println(sound + " - " + message) // <3>
        if (message >= 10) {
            referee.tell(message, sender) // <4>
        } else {
            sender.forEach(
                { actor: Actor<Int> -> actor.tell(message + 1, self())}, // <5>
                { referee.tell(message, sender) } // <6>
            )
        }
    }
}

private val semaphore = Semaphore(1) // <1>

fun main(args: Array<String>) {
    val referee = object : AbstractActor<Int>("Referee") {

        override fun onReceive(message: Int, sender: Result<Actor<Int>>) {
            println("Game ended after $message shots")
            semaphore.release() // <4>
        }
    }

    val player1 = Player("Player1", "Ping", referee)
    val player2 = Player("Player2", "Pong", referee)

    semaphore.acquire() // <2>
    player1.tell(1, Result(player2))
    semaphore.acquire() // <3>
    // <5>
}
