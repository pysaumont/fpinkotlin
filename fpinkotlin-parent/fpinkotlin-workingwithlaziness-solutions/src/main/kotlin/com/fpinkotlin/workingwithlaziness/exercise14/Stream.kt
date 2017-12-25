package com.fpinkotlin.workingwithlaziness.exercise14

import com.fpinkotlin.common.Result


sealed class Stream<out A> { // <1>

    abstract fun isEmpty(): Boolean

    abstract fun head(): Result<A> // <2>

    abstract fun tail(): Result<Stream<A>> // <3>

    abstract fun takeAtMost(n: Int): Stream<A>

    abstract fun dropAtMost(n: Int): Stream<A>

    private object Empty: Stream<Nothing>() {

        override fun takeAtMost(n: Int): Stream<Nothing> = this

        override fun dropAtMost(n: Int): Stream<Nothing> = this

        override fun head(): Result<Nothing> = Result()

        override fun tail(): Result<Nothing> = Result()

        override fun isEmpty(): Boolean = true

    }

    private class Cons<out A> (internal val hd: Lazy<A>, // <4> <5>
                               internal val tl: Lazy<Stream<A>>) : Stream<A>() {

        override fun takeAtMost(n: Int): Stream<A> = when {
            n > 0 -> cons(hd, Lazy { tl().takeAtMost(n - 1) })
            else -> Empty
        }

        override fun dropAtMost(n: Int): Stream<A> =  when {
            n > 0 -> tl().dropAtMost(n - 1)
            else -> Empty
        }

        override fun head(): Result<A> = Result(hd())

        override fun tail(): Result<Stream<A>> = Result(tl())

        override fun isEmpty(): Boolean = false
    }

    companion object {

        fun <A> cons(hd: Lazy<A>, tl: Lazy<Stream<A>>): Stream<A> = Cons(hd, tl) // <6>

        operator fun <A> invoke(): Stream<A> = Empty // <7>

        fun from(i: Int): Stream<Int> = cons(Lazy { i }, Lazy { from(i + 1) })

        fun <A> generate(seed: Lazy<A>, f: (A) -> A): Stream<A> = cons(seed, Lazy { generate(Lazy { f(seed()) }, f) })

        fun <A> generate(seed: A, f: (A) -> A): Stream<A> = cons(Lazy { seed }, Lazy { generate(Lazy { f(seed) }, f) })
    }
}

fun main(args: Array<String>) {
//    val stream = Stream.generate(0, Int::inc).takeAtMost(5)
    fun inc(i: Int): Int {
      println("generating ${i + 1}")
      return i + 1
    }
    val stream = Stream.generate(0, ::inc).takeAtMost(5)
    stream.head().forEach({ println(it) })
    stream.tail().flatMap { it.head() }.forEach({ println(it) })
    stream.tail().flatMap { it.tail().flatMap { it.head() } }.forEach({ println(it) })
    stream.head().forEach({ println(it) })
    stream.tail().flatMap { it.head() }.forEach({ println(it) })
    stream.tail().flatMap { it.tail().flatMap { it.head() } }.forEach({ println(it) })
}