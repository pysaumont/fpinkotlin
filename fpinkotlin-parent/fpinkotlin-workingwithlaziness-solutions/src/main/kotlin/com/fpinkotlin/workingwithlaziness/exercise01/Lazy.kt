package com.fpinkotlin.workingwithlaziness.exercise01


sealed class Lazy<out A>: () -> A {

    abstract fun isEvaluated(): Boolean

    internal class NonEvaluated<out A>(private val function: () -> A): Lazy<A>() {

        override fun invoke(): A = function()

        override fun isEvaluated() = false

        override fun toString(): String = "None"

        override fun equals(other: Any?): Boolean = false

        override fun hashCode(): Int = 0
    }

    internal class Evaluated<out A>(private val value: A) : Lazy<A>() {

        override fun invoke(): A = value

        override fun isEvaluated() = true

        override fun toString(): String = "Evaluated($value)"

        override fun equals(other: Any?): Boolean = when (other) {
            is Evaluated<*> -> value == other.value
            else -> false
        }

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    companion object {

        operator fun <A> invoke(function: () -> A): Lazy<A> = NonEvaluated(function)

        operator fun <A> invoke(value: A): Lazy<A> = Evaluated(value)
    }
}

fun main(args: Array<String>) {
    val first = Lazy { true }
    val second = Lazy { throw IllegalStateException() }
    println(first() || second())
    println(or(first, second))
}

fun or(a: () -> Boolean, b: () -> Boolean): Boolean = if (a()) true else b()

