package com.fpinkotlin.workingwithlaziness.exercise13

import com.fpinkotlin.common.Result
import java.util.*


sealed class Stream<out A> {

    abstract fun isEmpty(): Boolean

    abstract fun head(): Result<A>

    abstract fun tail(): Result<Stream<A>>

    abstract fun takeAtMost(n: Int): Stream<A>

    abstract fun dropAtMost(n: Int): Stream<A>

    private object Empty: Stream<Nothing>() {

        override fun takeAtMost(n: Int): Stream<Nothing> = this

        override fun dropAtMost(n: Int): Stream<Nothing> = this

        override fun head(): Result<Nothing> = Result()

        override fun tail(): Result<Nothing> = Result()

        override fun isEmpty(): Boolean = true

    }

    private class Cons<out A> (internal val hd: Lazy<A>,
                               internal val tl: Lazy<Stream<A>>) : Stream<A>() {

        override fun takeAtMost(n: Int): Stream<A> = when {
            n > 0 -> cons(hd, Lazy { tl().takeAtMost(n - 1) })
            else -> Empty
        }

        override fun dropAtMost(n: Int): Stream<A> =  when {
            n > 0 -> tl().dropAtMost(n - 1)
            else -> this
        }

        override fun head(): Result<A> = Result(hd())

        override fun tail(): Result<Stream<A>> = Result(tl())

        override fun isEmpty(): Boolean = false
    }

    companion object {

        fun <A> cons(hd: Lazy<A>, tl: Lazy<Stream<A>>): Stream<A> = Cons(hd, tl)

        operator fun <A> invoke(): Stream<A> = Empty

        fun from(i: Int): Stream<Int> = cons(Lazy { i }, Lazy { from(i + 1) })

        fun <A> repeat(f: () -> A): Stream<A> = cons(Lazy { f() }, Lazy { repeat(f) })
    }
}

fun main(args: Array<String>) {
    val stream = Stream.repeat(::random).dropAtMost(60000).takeAtMost(60000)
    stream.head().forEach(::println)
}

val rnd = Random()

fun random(): Int {
    val rnd = rnd.nextInt()
    println("evaluating $rnd")
    return rnd
}