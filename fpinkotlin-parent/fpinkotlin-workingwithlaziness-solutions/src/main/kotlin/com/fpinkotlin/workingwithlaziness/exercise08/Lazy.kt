package com.fpinkotlin.workingwithlaziness.exercise08

import com.fpinkotlin.common.List
import com.fpinkotlin.workingwithlaziness.exercise07.Lazy
import java.util.*

class Lazy<out A>(function: () -> A): () -> A {

    private val value: A by lazy(function)

    override fun invoke(): A = value

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


fun <A> sequence(lst: List<Lazy<A>>): Lazy<List<A>> = Lazy { lst.map { it() } }

fun main(args: Array<String>) {

    val name1: Lazy<String> = Lazy {
        println("Evaluating name1")
        "Mickey"
    }

    val name2: Lazy<String> = Lazy {
        println("Evaluating name2")
        "Donald"
    }

    val name3 = Lazy {
        println("Evaluating name3")
        "Goofy"
    }
    val list = sequence(List(name1, name2, name3))
    val defaultMessage = "No greetings when time is odd"
    val condition = Random(System.currentTimeMillis()).nextInt() % 2 == 0
    println(if (condition) list() else defaultMessage)
    println(if (condition) list() else defaultMessage)
}
