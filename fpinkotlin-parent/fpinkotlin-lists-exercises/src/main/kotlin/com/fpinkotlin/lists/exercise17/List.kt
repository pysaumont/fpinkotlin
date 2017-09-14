package com.fpinkotlin.lists.exercise17


sealed class List<A> {

    abstract fun isEmpty(): Boolean

    fun cons(a: A): List<A> = Cons(a, this)

    abstract fun setHead(a: A): List<A>

    fun drop(n: Int): List<A> = drop(this, n)

    fun dropWhile(p: (A) -> Boolean): List<A> = dropWhile(this, p)

    abstract fun init(): List<A>

    fun <B> foldRight(identity: B, f: (A) -> (B) -> B): B = foldRight(this, identity, f)

    fun <B> foldLeft(identity: B, f: (B) -> (A) -> B): B = foldLeft(identity, this, f)

    fun length(): Int = foldLeft(0) { { _ -> it + 1} }

    fun reverse(): List<A> = foldLeft(List.invoke(), { acc -> { acc.cons(it) } })

    fun <B> foldRightViaFoldLeft(identity: B, f: (A) -> (B) -> B): B =
        this.reverse().foldLeft(identity) { x -> { y -> f(y)(x) } }

    fun <B> cofoldRight(identity: B, f: (A) -> (B) -> B): B = cofoldRight(identity, this.reverse(), identity, f)

    fun concat(list: List<A>): List<A> = concat(this, list)

    fun concat_(list: List<A>): List<A> = concat_(this, list)

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

        fun <A, B> foldRight(list: List<A>, n: B, f: (A) -> (B) -> B): B =
            when (list) {
                is List.Nil -> n
                is List.Cons -> f(list.head)(foldRight(list.tail, n, f))
            }

        tailrec fun <A, B> foldLeft(acc: B, list: List<A>, f: (B) -> (A) -> B): B =
            when (list) {
                is List.Nil -> acc
                is List.Cons -> foldLeft(f(acc)(list.head), list.tail, f)
            }

        tailrec fun <A, B> cofoldRight(acc: B, list: List<A>, identity: B, f: (A) -> (B) -> B): B =
            when (list) {
                is List.Nil -> acc
                is List.Cons -> cofoldRight(f(list.head)(acc), list.tail, identity, f)
            }

        fun <A> concat_(list1: List<A>, list2: List<A>): List<A> = foldRight(list1, list2) { x -> { y -> Cons(x, y) } }

        fun <A> concat(list1: List<A>, list2: List<A>): List<A> = list1.reverse().foldLeft(list2) { x -> x::cons }

        fun <A> flatten(list: List<List<A>>): List<A> = foldRight(list, Nil()) { x -> x::concat }

        operator fun <A> invoke(vararg az: A): List<A> =
            az.foldRight(Nil(), { a: A, list: List<A> -> Cons(a, list) })
    }
}

fun sum(list: List<Int>): Int = list.foldLeft(0, { x -> { y -> x + y } })

fun product(list: List<Double>): Double = list.foldLeft(1.0, { x -> { y -> x * y } })

fun triple(list: List<Int>): List<Int> = List.foldRight(list, List<Int>()) { h -> { t -> t.cons(h * 3) } }

fun doubleToString(list: List<Double>): List<String> = TODO("doubleToString")
