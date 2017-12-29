package com.fpinkotlin.workingwithlaziness.exercise06

import java.util.*


class Lazy<out A>(function: () -> A): () -> A {

    private val value: A by lazy(function)

    operator override fun invoke(): A = value

    fun <B> map(f: (A) -> B): Lazy<B> = Lazy{ f(value) }

    companion object {

        operator fun <A> invoke(function: () -> A): Lazy<A> = Lazy(function)

    }
}

fun <A, B, C> lift2(f: (A) -> (B) -> C): (Lazy<A>) ->  (Lazy<B>) -> Lazy<C> =
        { ls1 ->
            { ls2 ->
                Lazy { f(ls1())(ls2()) }
            }
        }

fun main(args: Array<String>) {

    val greets: (String) -> String = { "Hello, $it!" }

    val name: Lazy<String> = Lazy {
        println("Evaluating name")
        "Mickey"
    }
    val defaultMessage = Lazy {
        println("Evaluating default message")
        "No greetings when time is odd"
    }
    val message = name.map(greets)
    val condition = Random(System.currentTimeMillis()).nextInt() % 2 == 0
    println(if (condition) message() else defaultMessage())
    println(if (condition) message() else defaultMessage())
}
