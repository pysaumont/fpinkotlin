package com.fpinkotlin.actors.coroutines.example22

import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking


fun produceNumbers() = produce<Int> {
    var x = 1 // start from 1
    while (true) {
        println("Producer producing $x")
        send(x++) // produce next
        delay(100) // wait 0.1s
    }
}

fun launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {
    channel.consumeEach {
        println("Processor #$id received $it")
    }
}

fun main(args: Array<String>) = runBlocking<Unit> {
    val producer = produceNumbers()
    repeat(5) { launchProcessor(it, producer) }
    delay(950)
    producer.cancel() // cancel producer coroutine and thus kill them all
}