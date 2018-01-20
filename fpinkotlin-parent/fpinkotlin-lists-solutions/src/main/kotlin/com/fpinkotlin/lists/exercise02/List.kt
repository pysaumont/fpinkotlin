package com.fpinkotlin.lists.exercise02

sealed class List<A> {

    abstract fun isEmpty(): Boolean

    fun cons(a: A): List<A>  = Cons(a, this)

    fun setHead(a: A): List<A> = when (this) {
        Nil -> throw IllegalStateException("setHead called on an empty list")
        is Cons -> tail.cons(a)
    }

    private object Nil: List<Nothing>() {

        override fun isEmpty() = true

        override fun toString(): String = "[NIL]"
    }

    private class Cons<A>(internal val head: A, internal val tail: List<A>) : List<A>() {

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
