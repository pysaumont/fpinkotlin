package com.fpinkotlin.advancedtrees.listing03

import com.fpinkotlin.common.Result

sealed class Heap<out A: Comparable<@UnsafeVariance A>> {

    internal abstract val left: Result<Heap<A>>  // <1>
    internal abstract val right: Result<Heap<A>>  // <1>
    internal abstract val head: Result<A>  // <1>
    protected abstract val rank: Int
    abstract val size: Int  // <2>
    abstract val isEmpty: Boolean

    abstract class Empty<out A: Comparable<@UnsafeVariance A>>: Heap<A>() { // <3>

        override val isEmpty: Boolean = true

        override val left: Result<Heap<A>> = Result(E)

        override val right: Result<Heap<A>> = Result(E)

        override val head: Result<A> = Result.failure("head() called on empty heap")

        override val rank: Int = 0

        override val size: Int = 0
    }

    internal object E: Empty<Nothing>() // <4>

    internal class H<out A: Comparable<@UnsafeVariance A>>(override val rank: Int, // <5>
                                                           private val lft: Heap<A>,
                                                           private val hd: A,
                                                           private val rght: Heap<A>): Heap<A>()  {

        override val isEmpty: Boolean = false

        override val left: Result<Heap<A>> = Result(lft)

        override val right: Result<Heap<A>> = Result(rght)

        override val head: Result<A> = Result(hd)

        override val size: Int = lft.size + rght.size + 1
    }

    companion object {

        operator fun <A: Comparable<A>> invoke(): Heap<A> = E // <6>
    }
}