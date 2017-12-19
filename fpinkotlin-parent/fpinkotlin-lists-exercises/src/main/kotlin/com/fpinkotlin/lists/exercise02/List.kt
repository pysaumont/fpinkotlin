package com.fpinkotlin.lists.exercise02

sealed class List<A> {

    abstract fun isEmpty(): Boolean

    fun cons(a: A): List<A>  = Cons(a, this)

    fun setHead(a: A): List<A> = TODO("setHead")

    private class Nil<A> : List<A>() {

        override fun isEmpty() = true

        override fun toString(): String = "[NIL]"

        override fun equals(other: Any?): Boolean = other is Nil<*>

        override fun hashCode(): Int = 0
    }

    private class Cons<A>(val head: A, val tail: List<A>) : List<A>() {

        override fun isEmpty() = false

        override fun toString(): String = "[${toString("", this)}NIL]"

        tailrec private fun toString(acc: String, list: List<A>): String = when (list) {
            is Nil -> acc
            is Cons -> toString("$acc${list.head}, ", list.tail)
        }
    }

    companion object {

        operator fun <A> invoke(vararg az: A): List<A> =
                az.foldRight(Nil(), { a: A, list: List<A> -> Cons(a, list) })
    }
}

fun main(args: Array<String>) {
    println(List<Any>())
    println(List(1, 2, 3))
    val numbers: Array<Int> = arrayOf(1, 2, 3, 4, 5)
    println(List(*numbers))
}