package com.fpinkotlin.lists.exercise06

sealed class List<out A> {

    abstract fun isEmpty(): Boolean

    abstract fun init(): List<A>

    fun setHead(a: @UnsafeVariance A): List<A> = when (this) {
        is Cons -> Cons(a, this.tail)
        is Nil -> throw IllegalStateException("setHead called on an empty list")
    }

    fun cons(a: @UnsafeVariance A): List<A> = Cons(a, this)

    fun concat(list: List<@UnsafeVariance A>): List<A> = concat(this, list)

    fun drop(n: Int): List<A> = drop(this, n)

    fun dropWhile(p: (A) -> Boolean): List<A> = dropWhile(this, p)

    fun reverse(): List<A> = reverse(List.invoke(), this)

    fun reverse2(): List<A> {
        tailrec fun <A> reverse2(acc: List<A>, list: List<A>): List<A> = when (list) {
            is Nil -> acc
            is Cons -> reverse2(Cons(list.head, acc), list.tail)
        }
        return reverse2(List.invoke(), this)
    }

    internal object Nil: List<Nothing>() {

        override fun init(): List<Nothing> = throw IllegalStateException("init called on an empty list")

        override fun isEmpty() = true

        override fun toString(): String = "[NIL]"

        override fun equals(other: Any?): Boolean = other is Nil

        override fun hashCode(): Int = 0
    }

    internal class Cons<out A>(internal val head: A, internal val tail: List<A>): List<A>() {

        override fun init(): List<A> = reverse().drop(1).reverse()

        override fun isEmpty() = false

        override fun toString(): String = "[${toString("", this)}NIL]"

        tailrec private fun toString(acc: String, list: List<A>): String = when (list) {
            is Nil  -> acc
            is Cons -> toString("$acc${list.head}, ", list.tail)
        }
    }

    companion object {

        fun <A> cons(a: A, list: List<A>): List<A> = Cons(a, list)

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
            is Cons -> Cons(list1.head, concat(list1.tail, list2))
        }

        tailrec fun <A> reverse(acc: List<A>, list: List<A>): List<A> = when (list) {
            is Nil -> acc
            is Cons -> reverse(Cons(list.head, acc), list.tail)
        }

        operator fun <A> invoke(vararg az: A): List<A> =
                az.foldRight(Nil, { a: A, list: List<A> -> Cons(a, list) })
    }
}

fun sum(ints: List<Int>): Int = TODO("sum")
