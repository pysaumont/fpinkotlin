package com.fpinkotlin.advancedtrees.exercise06

import com.fpinkotlin.common.Result

sealed class Heap<out A: Comparable<@UnsafeVariance A>> {

    internal abstract val left: Result<Heap<A>>

    internal abstract val right: Result<Heap<A>>

    protected abstract val rank: Int

    abstract val head: Result<A>

    abstract val size: Int

    abstract val isEmpty: Boolean

    abstract fun tail(): Result<Heap<A>>

    operator fun plus(element: @UnsafeVariance A): Heap<A> = merge(this, Heap(element))

    abstract class Empty<out A: Comparable<@UnsafeVariance A>>: Heap<A>() {

        override val isEmpty: Boolean = true

        override val left: Result<Heap<A>> = Result(E)

        override val right: Result<Heap<A>> = Result(E)

        override val head: Result<A> = Result.failure("head() called on empty heap")

        override val rank: Int = 0

        override val size: Int = 0

        override fun tail(): Result<Heap<A>> = Result.failure(IllegalStateException("tail() called on empty heap"))
    }

    internal object E: Empty<Nothing>()

    internal class H<out A: Comparable<@UnsafeVariance A>>(override val rank: Int, // <3>
                                                           private val lft: Heap<A>,
                                                           private val hd: A,
                                                           private val rght: Heap<A>): Heap<A>()  {

        override val isEmpty: Boolean = false

        override val left: Result<Heap<A>> = Result(lft)

        override val right: Result<Heap<A>> = Result(rght)

        override val head: Result<A> = Result(hd)

        override val size: Int = lft.size + rght.size + 1

        override fun tail(): Result<Heap<A>> = Result(merge(lft, rght))
    }

    companion object {

        operator fun <A: Comparable<A>> invoke(): Heap<A> = E

        operator fun <A: Comparable<A>> invoke(element: A): Heap<A> = H(1, E, element, E)

        protected fun <A : Comparable<A>> merge(head: A, first: Heap<A>, second: Heap<A>): Heap<A> =
            when {
                first.rank >= second.rank -> H(second.rank + 1, first, head, second)
                else -> H(first.rank + 1, second, head, first)
            }

        fun <A: Comparable<A>> merge(first: Heap<A>, second: Heap<A>): Heap<A> =
            first.head.flatMap { fh ->
                second.head.flatMap { sh ->
                    when {
                        fh <= sh -> first.left.flatMap { fl ->
                            first.right.map { fr ->
                                merge(fh, fl, merge(fr, second))
                            }
                        }
                        else -> second.left.flatMap { sl ->
                            second.right.map { sr ->
                                merge(sh, sl, merge(first, sr))
                            }
                        }
                    }
                }
            }.getOrElse(when (first) {
                            E -> second
                            else -> first
                        })
    }
}