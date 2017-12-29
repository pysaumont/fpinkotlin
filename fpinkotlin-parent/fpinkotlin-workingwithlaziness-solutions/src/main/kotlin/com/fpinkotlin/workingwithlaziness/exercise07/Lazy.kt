package com.fpinkotlin.workingwithlaziness.exercise07

import java.util.*


class Lazy<out A>(function: () -> A): () -> A {

    private val value: A by lazy(function)

    operator override fun invoke(): A = value

    fun <B> map(f: (A) -> B): Lazy<B> = Lazy { f(value) }

    fun <B> flatMap(f: (A) -> Lazy<B>): Lazy<B> = Lazy { f(value)() }

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

    val greetings = Lazy {
        println("Evaluating greetings")
        "Hello"
    }

    val flatGreets: (String) -> Lazy<String> = { name -> greetings.map { "$it, $name!"} }

    val name: Lazy<String> = Lazy {
        println("Evaluating name")
        "Donald"
    }
    val defaultMessage = Lazy {
        println("Evaluating default message")
        "No greetings when time is odd"
    }
    val message = name.flatMap(flatGreets)
    val condition = Random(System.currentTimeMillis()).nextInt() % 2 == 0
    println(if (condition) message() else defaultMessage())
    println(if (condition) message() else defaultMessage())
}
