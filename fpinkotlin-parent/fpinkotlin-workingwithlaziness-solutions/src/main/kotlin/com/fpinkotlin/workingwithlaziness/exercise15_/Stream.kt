package com.fpinkotlin.workingwithlaziness.exercise15_

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.common.cons
import com.fpinkotlin.workingwithlaziness.exercise15_.Stream.Companion.toList


sealed class Stream<out A> {

    abstract fun isEmpty(): Boolean

    abstract fun head(): Result<A>

    abstract fun tail(): Result<Stream<A>>

    fun take(n: Int): Stream<A> = take(n, Empty, this)

    abstract fun takeAtMost(n: Int): Stream<A>

    abstract fun dropAtMost(n: Int): Stream<A>

    fun <B> foldRight(identity: B, f: (A) -> (B) -> B) = foldRight(this, identity, f)

    fun <B> foldLeft(identity: B, f: (B) -> (A) -> B) = foldLeft(this, identity, f)

    private object Empty: Stream<Nothing>() {

        override fun takeAtMost(n: Int): Stream<Nothing> = this

        override fun dropAtMost(n: Int): Stream<Nothing> = this

        override fun head(): Result<Nothing> = Result()

        override fun tail(): Result<Nothing> = Result()

        override fun isEmpty(): Boolean = true

    }

    private class Cons<out A> (internal val hd: Lazy<A>,
                               internal val tl: Lazy<Stream<A>>) : Stream<A>() {

        override fun dropAtMost(n: Int): Stream<A> = dropAtMost(this, n)

        override fun takeAtMost(n: Int): Stream<A> = when {
            n > 0 -> cons(hd, Lazy { tl().takeAtMost(n - 1) })
            else -> Empty
        }


        override fun head(): Result<A> = Result(hd())

        override fun tail(): Result<Stream<A>> = Result(tl())

        override fun isEmpty(): Boolean = false

    }

    companion object {

        fun <A> cons(hd: Lazy<A>, tl: Lazy<Stream<A>>): Stream<A> = Cons(hd, tl)

        operator fun <A> invoke(): Stream<A> = Empty

        fun from(i: Int): Stream<Int> = cons(Lazy { i }, Lazy { from(i + 1) })

        tailrec fun <A> take(n: Int, acc: Stream<A>, stream: Stream<A>): Stream<A> = when {
            n > 0 -> when (stream) {
                is Empty -> Empty
                is Cons -> take(n - 1, cons(stream.hd, Lazy { stream.tl() }), stream.tl())
            }
            else -> acc
        }

        tailrec fun <A> dropAtMost(stream: Stream<A>, n: Int): Stream<A> = when (stream) {
            is Empty -> stream
            is Cons ->  if (n <= 0)
                stream
            else
                dropAtMost(stream.tl(), n - 1)
        }

//        fun <A> generate__(seed: Lazy<A>, f: (A) -> A): Stream<A> = cons(seed, Lazy { generate__(Lazy { f(seed()) }, f) })
//
//        fun <A> generate_(seed: Lazy<A>, f: (A) -> A): Lazy<Stream<A>> = Lazy { cons(seed, generate_(Lazy { f(seed()) }, f) ) }

        fun <A> generate(seed: A, f: (A) -> A): Stream<A> = cons(Lazy { seed }, Lazy { generate(f(seed), f) })

        fun <A, B> foldRight(stream: Stream<A>, identity: B, f: (A) -> (B) -> B): B =
            when (stream) {
                is Stream.Empty -> identity
                is Stream.Cons -> f(stream.hd())(foldRight(stream.tl(), identity, f))
            }

        tailrec fun <A, B> foldLeft(stream: Stream<A>, acc: B, f: (B) -> (A) -> B): B =
            when (stream) {
                is Stream.Empty -> acc
                is Stream.Cons -> foldLeft(stream.tl(), f(acc)(stream.hd()), f)
            }

        fun <A> toList(stream: Stream<A>) : List<A> {
            tailrec fun <A> toList(list: List<A>, stream: Stream<A>) : List<A> = when (stream) {
                is Empty -> list
                is Cons -> toList(list.cons(stream.hd()), stream.tl())
            }
            return toList(List(), stream).reverse()
        }
    }
}

fun main(args: Array<String>) {
//    val stream = Stream.generate(0, Int::inc).takeAtMost(5)
    fun inc(i: Int): Int {
      println("generating ${i + 1}")
      return i + 1
    }
    val stream = Stream.generate(0, ::inc).dropAtMost(60000).takeAtMost(60000)
    stream.head().forEach(::println)
    println(toList(stream))
//    stream.tail().flatMap { it.head() }.forEach({ println(it) })
//    stream.tail().flatMap { it.tail().flatMap { it.head() } }.forEach({ println(it) })
//    stream.head().forEach({ println(it) })
//    stream.tail().flatMap { it.head() }.forEach({ println(it) })
//    stream.tail().flatMap { it.tail().flatMap { it.head() } }.forEach({ println(it) })
    val list = stream.foldLeft(List<Int>()) { lst -> { i -> lst.cons(i)}}
    println(list)
    stream.foldRight(List<Int>()) { i -> { lst -> lst.cons(i)}}.headSafe().forEach(::println)
//    val x = stream.foldRight(List<Int>()) { i -> { lst -> lst.cons(i)}}.foldLeft(0) { a -> { b -> a + b }}
//    println(x)
}