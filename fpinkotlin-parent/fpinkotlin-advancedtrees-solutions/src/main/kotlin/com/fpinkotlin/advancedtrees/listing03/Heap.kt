package com.fpinkotlin.advancedtrees.listing03

import com.fpinkotlin.advancedtrees.common.Result

sealed class Heap<out A: Comparable<@UnsafeVariance A>> {

    protected abstract val left: Result<Heap<A>>  // <1>
    protected abstract val right: Result<Heap<A>>  // <1>
    protected abstract val rank: Int
    abstract val head: Result<A>  // <1>
    abstract val length: Int  // <2>
    abstract fun isEmpty(): Boolean

    internal object Empty: Heap<Nothing>() {

        override val left: Result<Heap<Nothing>> by lazy { Result(Empty) }

        override val right: Result<Heap<Nothing>> by lazy { Result(Empty) }

        override val rank: Int = 0

        override val head: Result<Nothing> by lazy { Result.failure<Nothing>("head() called on empty heap") }

        override val length: Int = 0

        override fun isEmpty(): Boolean = true
    }

    internal class H<out A: Comparable<@UnsafeVariance A>>(override val length: Int, // <3>
                                                           override val rank: Int, // <3>
                                                           private val lft: Heap<A>,
                                                           private val hd: A,
                                                           private val rght: Heap<A>): Heap<A>()  {

        override val left: Result<Heap<A>> = Result(lft)

        override val right: Result<Heap<A>> = Result(rght)

        override val head: Result<A> = Result(hd)

        override fun isEmpty(): Boolean = false
    }

}