package com.fpinkotlin.lists.listing01

sealed class List<A> {

    abstract fun isEmpty(): Boolean

    private object Nil : List<Nothing>() {

        override fun isEmpty() = true

        override fun toString(): String = "[NIL]"
    }

    private class Cons<A>(private val head: A, private val tail: List<A>) : List<A>() {

        override fun isEmpty() = false

        override fun toString(): String = "[${toString("", this)}NIL]"

        private tailrec fun toString(acc: String, list: List<A>): String = when (list) {
            Nil -> acc
            is Cons -> toString("$acc${list.head}, ", list.tail)
        }
    }

    companion object {

        operator fun <A> invoke(vararg az: A): List<A> =
                az.foldRight(Nil as List<A>, { a: A, list: List<A> -> Cons(a, list) })
    }
}
