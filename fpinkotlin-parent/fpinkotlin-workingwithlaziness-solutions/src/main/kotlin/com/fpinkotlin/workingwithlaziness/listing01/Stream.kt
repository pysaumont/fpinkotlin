package com.fpinkotlin.workingwithlaziness.listing01

import com.fpinkotlin.common.Result


sealed class Stream<out A> {

    abstract fun isEmpty(): Boolean

    abstract fun head(): Result<A>

    abstract fun tail(): Result<Stream<A>>

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
    }
}

fun main(args: Array<String>) {
    val stream = Stream.from(1)
    stream.head().forEach(onSuccess = { println(it) })
    stream.tail().flatMap { it.head() }.forEach(onSuccess = { println(it) })
    stream.tail().flatMap { it.tail().flatMap { it.head() } }.forEach(onSuccess = { println(it) })
    stream.head().forEach(onSuccess = { println(it) })
    stream.tail().flatMap { it.head() }.forEach(onSuccess = { println(it) })
    stream.tail().flatMap { it.tail().flatMap { it.head() } }.forEach(onSuccess = { println(it) })
}