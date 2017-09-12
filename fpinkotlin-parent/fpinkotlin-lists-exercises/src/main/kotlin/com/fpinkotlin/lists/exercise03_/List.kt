package com.fpinkotlin.lists.exercise03_

sealed class List<A> {

    abstract fun isEmpty(): Boolean

    fun cons(a: A): List<A>  = Cons(a, this)

    abstract fun setHead(a: A): List<A>

    abstract fun head(): A

    abstract fun tail(): List<A>

    private class Nil<A> : List<A>() {

        override fun head(): A = TODO("head")

        override fun tail(): List<A> = TODO("tail")

        override fun setHead(a: A): List<A> = throw IllegalStateException("setHead called on an empty list")

        override fun isEmpty() = true

        override fun toString(): String = "[NIL]"

        override fun equals(other: Any?): Boolean = other is Nil<*>

        override fun hashCode(): Int = 0
    }

    private class Cons<A>(private val head: A, private val tail: List<A>) : List<A>() {

        override fun head(): A = TODO("head")

        override fun tail(): List<A> = TODO("tail")

        override fun setHead(a: A): List<A> = tail.cons(a)

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
