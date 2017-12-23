package com.fpinkotlin.workingwithlaziness.exercise06

import java.util.*


class Lazy<out A>(function: () -> A): () -> A {

    private val value: A by lazy(function)

    override fun invoke(): A = value

    fun <B> map(f: (A) -> B): Lazy<B> = Lazy{ f(value) }

    fun <B> flatMap(f: (A) -> Lazy<B>): Lazy<B> = Lazy{ f(value)() }

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
    val greets: (String) -> String = { "Hello, $it!" }

    val greetings = Lazy {
        println("computing greetings")
        "Hello"
    }

    val flatGreets: (String) -> Lazy<String> = { name -> greetings.map { "$it, $name!"} }

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
    val message1 = name1.map(greets)
    val message2 = name2.flatMap(flatGreets)
    val condition = Random(System.currentTimeMillis()).nextInt() % 2 == 0
    println(if (condition) message1() else defaultMessage())
    println(if (condition) message1() else defaultMessage())
    println(if (condition) message2() else defaultMessage())
}
