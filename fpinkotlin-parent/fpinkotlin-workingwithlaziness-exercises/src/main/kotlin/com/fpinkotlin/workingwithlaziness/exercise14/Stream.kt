package com.fpinkotlin.workingwithlaziness.exercise14

import com.fpinkotlin.common.Result


sealed class Stream<out A> {

    abstract fun isEmpty(): Boolean

    abstract fun head(): Result<A>

    abstract fun tail(): Result<Stream<A>>

    abstract fun takeAtMost(n: Int): Stream<A>

    abstract fun dropAtMost(n: Int): Stream<A>

    private object Empty : Stream<Nothing>() {

        override fun takeAtMost(n: Int): Stream<Nothing> = this

        override fun head(): Result<Nothing> = Result()

        override fun tail(): Result<Nothing> = Result()

        override fun isEmpty(): Boolean = true

        override fun dropAtMost(n: Int): Stream<Nothing> = this

    }

    private class Cons<out A>(internal val hd: Lazy<A>,
                              internal val tl: Lazy<Stream<A>>) : Stream<A>() {

        override fun takeAtMost(n: Int): Stream<A> = when {
            n > 0 -> cons(hd, Lazy { tl().takeAtMost(n - 1) })
            else -> Empty
        }

        override fun head(): Result<A> = Result(hd())

        override fun tail(): Result<Stream<A>> = Result(tl())

        override fun isEmpty(): Boolean = false

        override fun dropAtMost(n: Int): Stream<A> {
            tailrec fun dropAtMost(n: Int, stream: Stream<A>): Stream<A> = when(n) {
                0 -> stream
                else -> when(stream) {
                    is Empty -> stream
                    is Cons -> dropAtMost(n - 1, stream.tl())
                }
            }
            return dropAtMost(n, this)
        }
    }

    companion object {

        fun <A> cons(hd: Lazy<A>, tl: Lazy<Stream<A>>): Stream<A> = Cons(hd, tl)

        operator fun <A> invoke(): Stream<A> = Empty

        fun from(i: Int): Stream<Int> = cons(Lazy { i }, Lazy { from(i + 1) })

        fun <A> repeat(f: () -> A): Stream<A> = cons(Lazy { f() }, Lazy { repeat(f) })
    }
}
