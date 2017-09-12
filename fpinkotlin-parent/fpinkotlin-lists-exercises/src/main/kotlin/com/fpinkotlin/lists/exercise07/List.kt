package com.fpinkotlin.lists.exercise07

sealed class List<A> {

    abstract fun isEmpty(): Boolean

    fun cons(a: A): List<A> = Cons(a, this)

    abstract fun setHead(a: A): List<A>

    fun drop(n: Int): List<A> = drop(this, n)

    fun dropWhile(p: (A) -> Boolean): List<A> = dropWhile(this, p)

    fun concat(list: List<A>): List<A> = concat(this, list)

    fun reverse(): List<A> = reverse(Nil(), this)

    fun reverse2_(): List<A> {
        tailrec fun <A> reverse2_(acc: List<A>, list: List<A>): List<A> = when (list) {
            is Nil -> acc
            is Cons -> reverse2_(acc.cons(list.head), list.tail)
        }
        return reverse2_(Nil(), this)
    }

    abstract fun init(): List<A>

    internal class Nil<A>: List<A>() {

        override fun init(): List<A> = throw IllegalStateException("init called on an empty list")

        override fun setHead(a: A): List<A> = throw IllegalStateException("setHead called on an empty list")

        override fun isEmpty() = true

        override fun toString(): String = "[NIL]"

        override fun equals(other: Any?): Boolean = other is Nil<*>

        override fun hashCode(): Int = 0
    }

    internal class Cons<A>(val head: A, val tail: List<A>): List<A>() {

        override fun init(): List<A> = reverse().drop(1).reverse()

        override fun setHead(a: A): List<A> = tail.cons(a)

        override fun isEmpty() = false

        override fun toString(): String = "[${toString("", this)}NIL]"

        tailrec private fun toString(acc: String, list: List<A>): String = when (list) {
            is Nil  -> acc
            is Cons -> toString("$acc${list.head}, ", list.tail)
        }
    }

    companion object {

        tailrec fun <A> drop(list: List<A>, n: Int): List<A> = when (list) {
            is Nil -> list
            is Cons -> if (n <= 0) list else drop(list.tail, n - 1)
        }

        tailrec fun <A> dropWhile(list: List<A>, p: (A) -> Boolean): List<A> = when (list) {
            is Nil -> list
            is Cons -> if (p(list.head)) dropWhile(list.tail, p) else list
        }

        fun <A> concat(list1: List<A>, list2: List<A>): List<A> = when (list1) {
            is Nil -> list2
            is Cons -> concat(list1.tail, list2).cons(list1.head)
        }

        tailrec fun <A> reverse(acc: List<A>, list: List<A>): List<A> = when (list) {
            is Nil -> acc
            is Cons -> reverse(acc.cons(list.head), list.tail)
        }

        fun sum(ints: List<Int>): Int = when (ints) {
            is Nil -> 0
            is Cons -> ints.head + sum(ints.tail)
        }

        operator fun <A> invoke(vararg az: A): List<A> =
            az.foldRight(Nil(), { a: A, list: List<A> -> Cons(a, list) })
    }
}

fun sum(ints: List<Int>): Int = when (ints) {
    is List.Nil  -> 0
    is List.Cons -> ints.head + sum(ints.tail)
}

fun product(ints: List<Double>): Double  = TODO("product")
