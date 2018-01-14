package com.fpinkotlin.advancedtrees.listing03

import com.fpinkotlin.advancedtrees.common.Result

sealed class Heap<out A: Comparable<@UnsafeVariance A>> {

    protected abstract fun left(): Result<Heap<A>>  // <1>
    protected abstract fun right(): Result<Heap<A>>  // <1>
    protected abstract fun rank(): Int
    abstract fun head(): Result<A>  // <1>
    abstract fun length(): Int  // <2>
    abstract fun isEmpty(): Boolean

    internal object Empty: Heap<Nothing>() {

        override fun left(): Result<Heap<Nothing>> = Result(Empty)

        override fun right(): Result<Heap<Nothing>> = Result(Empty)

        override fun rank(): Int = 0

        override fun head(): Result<Nothing> =
                Result.failure(NoSuchElementException("head() called on empty heap"))

        override fun length(): Int = 0

        override fun isEmpty(): Boolean = true
    }

    internal class H<out A: Comparable<@UnsafeVariance A>>(internal val length: Int, // <3>
                                                           internal val rank: Int, // <3>
                                                           internal val left: Heap<A>,
                                                           internal val head: A,
                                                           internal val right: Heap<A>): Heap<A>()  {

        override fun left(): Result<Heap<A>> = Result(left)

        override fun right(): Result<Heap<A>> = Result(right)

        override fun rank(): Int = rank

        override fun head(): Result<A> = Result(head)

        override fun length(): Int = length

        override fun isEmpty(): Boolean = false
    }

}