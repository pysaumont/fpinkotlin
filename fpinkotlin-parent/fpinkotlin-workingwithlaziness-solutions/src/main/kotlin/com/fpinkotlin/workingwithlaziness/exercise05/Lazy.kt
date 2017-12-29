package com.fpinkotlin.workingwithlaziness.exercise05

import java.util.*


class Lazy<out A>(function: () -> A): () -> A {

    private val value: A by lazy(function)

    operator override fun invoke(): A = value

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

val consMessage: (String) -> (String) -> String =
    { greetings ->
        { name ->
            "$greetings, $name!"
        }
    }

fun main(args: Array<String>) {
    val greetings = Lazy {
        println("Evaluating greetings")
        "Hello"
    }
    val name1: Lazy<String> = Lazy {
        println("Evaluating name")
        "Mickey"
    }
    val name2: Lazy<String> = Lazy {
        println("Evaluating name")
        "Donald"
    }
    val defaultMessage = Lazy {
        println("Evaluating default message")
        "No greetings when time is odd"
    }
    val greetingString = lift2(consMessage)(greetings)
    val message1 = greetingString(name1)
    val message2 = greetingString(name2)
    val condition = Random(System.currentTimeMillis()).nextInt() % 2 == 0
    println(if (condition) message1() else defaultMessage())
    println(if (condition) message1() else defaultMessage())
    println(if (condition) message2() else defaultMessage())
}
