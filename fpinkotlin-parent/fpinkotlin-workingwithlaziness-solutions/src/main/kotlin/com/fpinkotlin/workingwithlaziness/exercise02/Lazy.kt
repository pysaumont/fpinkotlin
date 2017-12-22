package com.fpinkotlin.workingwithlaziness.exercise02


class Lazy<out A>(function: () -> A): () -> A {

    private val value: A by lazy(function)

    override fun invoke(): A = value

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
    println(first() || second())
    println(first() || second())
    println(or(first, second))
}

fun or(a: Lazy<Boolean>, b: Lazy<Boolean>): Boolean = if (a()) true else b()

