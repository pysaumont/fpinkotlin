package com.fpinkotlin.workingwithlaziness.exercise15

import com.fpinkotlin.common.Result


sealed class Stream<out A> {

    abstract fun isEmpty(): Boolean

    abstract fun head(): Result<A>

    abstract fun tail(): Result<Stream<A>>

    fun takeAtMost(n: Int): Stream<A> = takeAtMost(n, Empty, this)

    fun dropAtMost(n: Int): Stream<A> = dropAtMost(n, this)

    fun <B> foldRight(identity: B, f: (A) -> (B) -> B) = foldRight(this, identity, f)

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
            else -> acc
        }

        tailrec fun <A> dropAtMost(n: Int, stream: Stream<A>): Stream<A> =  when {
            n > 0 -> when (stream) {
                is Empty -> stream
                is Cons -> dropAtMost(n - 1, stream.tl())
            }
            else -> stream
        }

        fun <A> generate(seed: Lazy<A>, f: (A) -> A): Stream<A> = cons(seed, Lazy { generate(Lazy { f(seed()) }, f) })

        fun <A> generate_(seed: Lazy<A>, f: (A) -> A): Lazy<Stream<A>> = Lazy { cons(seed, generate_(Lazy { f(seed()) }, f) ) }

        fun <A> generate(seed: A, f: (A) -> A): Stream<A> = cons(Lazy { seed }, Lazy { generate(Lazy { f(seed) }, f) })

        fun <A, B> foldRight(stream: Stream<A>, identity: B, f: (A) -> (B) -> B): B =
                when (stream) {
                    is Stream.Empty -> identity
                    is Stream.Cons -> f(stream.hd())(foldRight(stream.tl(), identity, f))
                }
    }
}

fun main(args: Array<String>) {
//    val stream = Stream.generate(0, Int::inc).takeAtMost(5)
    fun inc(i: Int): Int {
      println("generating ${i + 1}")
      return i + 1
    }
    val stream = Stream.generate(0, ::inc).dropAtMost(1000)//.takeAtMost(100)
    stream.head().forEach({ println(it) })
    stream.tail().flatMap { it.head() }.forEach({ println(it) })
    stream.tail().flatMap { it.tail().flatMap { it.head() } }.forEach({ println(it) })
    stream.head().forEach({ println(it) })
    stream.tail().flatMap { it.head() }.forEach({ println(it) })
    stream.tail().flatMap { it.tail().flatMap { it.head() } }.forEach({ println(it) })
}