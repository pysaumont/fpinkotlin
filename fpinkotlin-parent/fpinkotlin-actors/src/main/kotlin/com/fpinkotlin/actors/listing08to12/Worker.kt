package com.fpinkotlin.actors.listing08to12

import com.fpinkotlin.common.Result


class Worker(id: String) : AbstractActor<Int>(id) {

    override fun onReceive(message: Int, sender: Result<Actor<Int>>) {
        sender.forEach (onSuccess = { a: Actor<Int> ->
            a.tell(slowFibonacci(message), self())
        })
    }

    private fun slowFibonacci(number: Int): Int {
        return when (number) {
            0    -> 1
            1    -> 1
            else -> slowFibonacci(number - 1) + slowFibonacci(number - 2)
        }
    }

    private fun fibonacci(number: Int): Int {
        tailrec fun fibonacci(acc1: Int, acc2: Int, x: Int): Int = when (x) {
            0 -> 1
            1 -> acc1 + acc2
            else -> fibonacci(acc2, acc1 + acc2, x - 1)
        }
        return fibonacci(0, 1, number)
    }

}