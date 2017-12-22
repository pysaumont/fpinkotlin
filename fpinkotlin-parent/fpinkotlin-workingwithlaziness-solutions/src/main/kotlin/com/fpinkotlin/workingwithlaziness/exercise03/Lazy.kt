package com.fpinkotlin.workingwithlaziness.exercise03


class Lazy<out A>(function: () -> A) {

    val value: A by lazy(function)

    //override fun invoke(): A = value

    companion object {

        operator fun <A> invoke(function: () -> A): Lazy<A> = Lazy(function)
    }
}

fun main(args: Array<String>) {
    val first = Lazy {
        println("computing first")
        true
    }
    val second = Lazy {
        println("computing second")
        throw IllegalStateException()
    }
    println(first.value || second.value)
    println(first.value || second.value)
    println(or(first, second))
}

fun or(a: Lazy<Boolean>, b: Lazy<Boolean>): Boolean = if (a.value) true else b.value

