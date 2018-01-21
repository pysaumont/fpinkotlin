package com.fpinkotlin.workingwithlaziness.listing01

import com.fpinkotlin.common.Result


sealed class Stream<out A> { // <1>

    abstract fun isEmpty(): Boolean

    abstract fun head(): Result<A> // <2>

    abstract fun tail(): Result<Stream<A>> // <3>

    private object Empty: Stream<Nothing>() {

        override fun head(): Result<Nothing> = Result()

        override fun tail(): Result<Nothing> = Result()

        override fun isEmpty(): Boolean = true

    }

    private class Cons<out A> (internal val hd: Lazy<A>, // <4> <5>
                               internal val tl: Lazy<Stream<A>>) : Stream<A>() {

        override fun head(): Result<A> = Result(hd())

        override fun tail(): Result<Stream<A>> = Result(tl())

        override fun isEmpty(): Boolean = false
    }

    companion object {

        fun <A> cons(hd: Lazy<A>, tl: Lazy<Stream<A>>): Stream<A> = Cons(hd, tl) // <6>

        operator fun <A> invoke(): Stream<A> = Empty // <7>

        fun from(i: Int): Stream<Int> = cons(Lazy { i }, Lazy { from(i + 1) })
    }
}
