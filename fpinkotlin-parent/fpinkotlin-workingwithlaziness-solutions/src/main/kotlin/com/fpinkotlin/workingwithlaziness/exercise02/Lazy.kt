package com.fpinkotlin.workingwithlaziness.exercise02

import java.util.*


class Lazy<out A>(function: () -> A): () -> A {

    private val value: A by lazy(function)

    override fun invoke(): A = value

    companion object {

        operator fun <A> invoke(function: () -> A): Lazy<A> = Lazy(function)
    }
}


fun constructMessage(greetings: Lazy<String>, name: Lazy<String>): Lazy<String> = Lazy { "${greetings()}, ${name()}!" }

fun main(args: Array<String>) {
    val greetings = Lazy {
        println("computing greetings")
        "Hello"
    }
    val name1: Lazy<String> = Lazy {
        println("computing name")
        "Mickey"
    }
    val name2: Lazy<String> = Lazy {
        println("computing name")
        "Donald"
    }
    val defaultMessage = Lazy {
        println("computing default message")
        "No greetings when time is odd"
    }
    val message1 = constructMessage(greetings, name1)
    val message2 = constructMessage(greetings, name2)
    val condition = Random(System.currentTimeMillis()).nextInt() % 2 == 0
    println(if (condition) message1() else defaultMessage())
    println(if (condition) message1() else defaultMessage())
    println(if (condition) message2() else defaultMessage())
}