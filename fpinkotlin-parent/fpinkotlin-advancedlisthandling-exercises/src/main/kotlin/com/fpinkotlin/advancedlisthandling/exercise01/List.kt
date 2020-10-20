package com.fpinkotlin.advancedlisthandling.exercise01


sealed class List<out A> {
    internal abstract val length: Int

    abstract fun isEmpty(): Boolean

    abstract fun init(): List<A>

    fun lengthMemoized(): Int = length

    fun setHead(a: @UnsafeVariance A): List<A> = when (this) {
        Nil -> throw IllegalStateException("setHead called on an empty list")
        is Cons -> Cons(a, this.tail)
    }

    fun cons(a: @UnsafeVariance A): List<A> = Cons(a, this)

    fun concat(list: List<@UnsafeVariance A>): List<A> = concat(this, list)

    fun concatViaFoldRight(list: List<@UnsafeVariance A>): List<A> = concatViaFoldRight(this, list)

    fun drop(n: Int): List<A> = drop(this, n)

    fun dropWhile(p: (A) -> Boolean): List<A> = dropWhile(this, p)

    fun reverse(): List<A> = foldLeft(Nil as List<A>) { acc -> { acc.cons(it) } }

    fun <B> foldRight(identity: B, f: (A) -> (B) -> B): B = foldRight(this, identity, f)

    fun <B> foldLeft(identity: B, f: (B) -> (A) -> B): B = foldLeft(identity, this, f)

    fun length(): Int = foldLeft(0) { { _ -> it + 1} }

    fun <B> foldRightViaFoldLeft(identity: B, f: (A) -> (B) -> B): B =
            this.reverse().foldLeft(identity) { x -> { y -> f(y)(x) } }

    fun <B> coFoldRight(identity: B, f: (A) -> (B) -> B): B = coFoldRight(identity, this.reverse(), identity, f)

    fun <B> map(f: (A) -> B): List<B> = foldLeft(Nil) { acc: List<B> -> { h: A -> Cons(f(h), acc) } }.reverse()

    fun <B> flatMap(f: (A) -> List<B>): List<B> = flatten(map(f))

    fun filter(p: (A) -> Boolean): List<A> = flatMap { a -> if (p(a)) List(a) else Nil }

    internal object Nil: List<Nothing>() {

        override val length: Int = 0

        override fun init(): List<Nothing> = throw IllegalStateException("init called on an empty list")

        override fun isEmpty() = true

        override fun toString(): String = "[NIL]"
    }

    internal class Cons<out A>(internal val head: A,
                               internal val tail: List<A>): List<A>() {

        override val length: Int = tail.length + 1

        override fun init(): List<A> = reverse().drop(1).reverse()

        override fun isEmpty() = false

        override fun toString(): String = "[${toString("", this)}NIL]"

        private tailrec fun toString(acc: String, list: List<A>): String = when (list) {
            Nil  -> acc
            is Cons -> toString("$acc${list.head}, ", list.tail)
        }
    }

    companion object {

        fun <A> cons(a: A, list: List<A>): List<A> = Cons(a, list)

        tailrec fun <A> drop(list: List<A>, n: Int): List<A> = when (list) {
            Nil -> list
            is Cons -> if (n <= 0) list else drop(list.tail, n - 1)
        }

        tailrec fun <A> dropWhile(list: List<A>, p: (A) -> Boolean): List<A> = when (list) {
            Nil -> list
            is Cons -> if (p(list.head)) dropWhile(list.tail, p) else list
        }

        fun <A> concat(list1: List<A>, list2: List<A>): List<A> = list1.reverse().foldLeft(list2) { x -> x::cons }

        fun <A> concatViaFoldRight(list1: List<A>, list2: List<A>): List<A> = foldRight(list1, list2) { x -> { y -> Cons(x, y) } }

        fun <A, B> foldRight(list: List<A>, identity: B, f: (A) -> (B) -> B): B =
                when (list) {
                    Nil -> identity
                    is Cons -> f(list.head)(foldRight(list.tail, identity, f))
                }

        tailrec fun <A, B> foldLeft(acc: B, list: List<A>, f: (B) -> (A) -> B): B =
                when (list) {
                    Nil -> acc
                    is Cons -> foldLeft(f(acc)(list.head), list.tail, f)
                }

        tailrec fun <A, B> coFoldRight(acc: B, list: List<A>, identity: B, f: (A) -> (B) -> B): B =
                when (list) {
                    Nil -> acc
                    is Cons -> coFoldRight(f(list.head)(acc), list.tail, identity, f)
                }

        fun <A> flatten(list: List<List<A>>): List<A> = list.coFoldRight(Nil) { x -> x::concat }

        operator fun <A> invoke(vararg az: A): List<A> =
                az.foldRight(Nil) { a: A, list: List<A> -> Cons(a, list) }
    }
}

fun sum(list: List<Int>): Int = list.foldRight(0) { x -> { y -> x + y } }

fun product(list: List<Double>): Double = list.foldRight(1.0) { x -> { y -> x * y } }

fun triple(list: List<Int>): List<Int> =
        List.foldRight(list, List()) { h -> { t: List<Int> -> t.cons(h * 3) } }

fun doubleToString(list: List<Double>): List<String> =
        List.foldRight(list, List())  { h -> { t: List<String> -> t.cons(h.toString()) } }
