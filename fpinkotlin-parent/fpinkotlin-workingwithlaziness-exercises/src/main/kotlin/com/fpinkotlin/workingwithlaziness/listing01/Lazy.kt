package com.fpinkotlin.workingwithlaziness.listing01

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.common.sequence
import java.lang.IllegalStateException
import java.util.*

class Lazy<out A>(function: () -> A): () -> A {

    private val value: A by lazy(function)

    operator override fun invoke(): A = value

    fun <B> map(f: (A) -> B): Lazy<B> = Lazy { f(value) }

    fun <B> flatMap(f: (A) -> Lazy<B>): Lazy<B> = Lazy { f(value)() }

    fun forEach(condition: Boolean, ifTrue: (A) -> Unit, ifFalse: () -> Unit = {}) =
            if (condition)
                ifTrue(value)
            else
                ifFalse()

    fun forEach(condition: Boolean, ifTrue: () -> Unit = {}, ifFalse: (A) -> Unit) =
            if (condition)
                ifTrue()
            else
                ifFalse(value)

    fun forEach(condition: Boolean, ifTrue: (A) -> Unit, ifFalse: (A) -> Unit) =
            if (condition)
                ifTrue(value)
            else
                ifFalse(value)

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

fun <A> sequenceResult(lst: List<Lazy<A>>): Lazy<Result<List<A>>> =
        Lazy { sequence(lst.map { Result.of(it) }) }

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

    val name4 = Lazy {
        println("Evaluating name4")
        throw IllegalStateException("Exception while evaluating name4")
    }

    val list1 = sequenceResult(List(name1, name2, name3))
    val list2 = sequenceResult(List(name1, name2, name3, name4))
    val defaultMessage = "No greetings when time is odd"
    val condition = Random(System.currentTimeMillis()).nextInt() % 2 == 0
    val printMessage: (Result<List<String>>) -> Unit = ::println
    val printDefault: () -> Unit = { println(defaultMessage)}
    println(condition)
    println("===")
    list1.forEach(condition, printMessage, printDefault)
    list1.forEach(condition, printMessage, printDefault)
    list2.forEach(condition, printMessage, printDefault)
    list2.forEach(condition, printMessage, printDefault)
    println("===")
    list1.forEach(condition, printMessage)
    list1.forEach(condition, ifFalse = printMessage)
    list1.forEach(condition, printDefault, printMessage)
    list2.forEach(condition, printMessage)
    list2.forEach(condition, ifFalse = printMessage)
    list2.forEach(condition, printDefault, printMessage)
    println("===")
    list1.forEach(condition, printMessage, printMessage)
    list1.forEach(condition, printMessage, printMessage)
    list2.forEach(condition, printMessage, printMessage)
    list2.forEach(condition, printMessage, printMessage)
}
