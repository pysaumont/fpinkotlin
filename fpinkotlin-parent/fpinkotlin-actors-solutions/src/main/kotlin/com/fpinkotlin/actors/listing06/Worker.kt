package com.fpinkotlin.actors.listing06

import com.fpinkotlin.actors.listing01_02_03.AbstractActor
import com.fpinkotlin.actors.listing01_02_03.Actor
import com.fpinkotlin.common.Result


class Worker(id: String) : AbstractActor<Int>(id) {

    override fun onReceive(message: Int, sender: Result<Actor<Int>>) {
        sender.forEach(
                { a: Actor<Int> ->
                    a.tell(fibonacci(message), self()) // <1>
                })
    }

    private fun fibonacci(number: Int): Int {
        tailrec fun fibonacci(acc1: Int, acc2: Int, x: Int): Int = when (x) {
            0 -> 1
            1 -> acc1 + acc2
            else -> fibonacci(acc2, acc1 + acc2, x - 1) // <2>
        }
        return fibonacci(0, 1, number) // <3>
    }
}

