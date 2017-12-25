package com.fpinkotlin.workingwithlaziness.exercise13

import com.fpinkotlin.common.Result


sealed class Stream<out A> {

    abstract fun isEmpty(): Boolean

    abstract fun head(): Result<A>

    abstract fun tail(): Result<Stream<A>>

    fun takeAtMost(n: Int): Stream<A> = takeAtMost(n, Empty, this)

    fun dropAtMost(n: Int): Stream<A> = dropAtMost(n, this)

    private object Empty: Stream<Nothing>() {

        override fun head(): Result<Nothing> = Result()

        override fun tail(): Result<Nothing> = Result()

        override fun isEmpty(): Boolean = true

    }

    private class Cons<out A> (internal val hd: Lazy<A>,
                               internal val tl: Lazy<Stream<A>>) : Stream<A>() {

        override fun head(): Result<A> = Result(hd())

        override fun tail(): Result<Stream<A>> = Result(tl())

        override fun isEmpty(): Boolean = false
    }

    companion object {

        fun <A> cons(hd: Lazy<A>, tl: Lazy<Stream<A>>): Stream<A> = Cons(hd, tl)

        operator fun <A> invoke(): Stream<A> = Empty

        fun from(i: Int): Stream<Int> = cons(Lazy { i }, Lazy { from(i + 1) })

        tailrec fun <A> takeAtMost(n: Int, acc: Stream<A>, stream: Stream<A>): Stream<A> = when {
            n > 0 -> when (stream) {
                is Empty -> acc
                is Cons -> takeAtMost(n - 1, cons(stream.hd, Lazy { stream.tl() }), stream.tl())
            }
            else -> Empty
        }

        tailrec fun <A> dropAtMost(n: Int, stream: Stream<A>): Stream<A> =  when {
            n > 0 -> when (stream) {
                is Empty -> stream
                is Cons -> dropAtMost(n - 1, stream.tl())
            }
            else -> stream
        }

    }
}
