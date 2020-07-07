package com.fpinkotlin.actors.listing13

import com.fpinkotlin.common.Result


class Worker(id: String) : AbstractActor<Pair<Int, Int>>(id) {

    override fun onReceive(message: Pair<Int, Int>,
                           sender: Result<Actor<Pair<Int, Int>>>) {
        sender.forEach (onSuccess = { a: Actor<Pair<Int, Int>> ->
                    a.tell(Pair(fibonacci(message.first),
                                message.second) , self())
                })
    }

    private fun fibonacci(number: Int): Int {
        tailrec fun fibonacci(acc1: Int, acc2: Int, x: Int): Int = when (x) {
            0 -> 1
            1 -> acc1 + acc2
            else -> fibonacci(acc2, acc1 + acc2, x - 1)
        }
        return fibonacci(0, 1, number)
    }

    private fun slowFibonacci(number: Int): Int {
        return when (number) {
            0    -> 1
            1    -> 1
            else -> slowFibonacci(number - 1) + slowFibonacci(number - 2)
        }
    }
}

