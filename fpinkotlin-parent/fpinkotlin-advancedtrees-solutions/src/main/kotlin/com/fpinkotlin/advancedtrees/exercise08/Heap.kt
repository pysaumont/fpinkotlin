package com.fpinkotlin.advancedtrees.exercise08

import com.fpinkotlin.advancedtrees.common.Option
import com.fpinkotlin.advancedtrees.common.Result
import com.fpinkotlin.advancedtrees.common.getOrElse
import com.fpinkotlin.advancedtrees.common.orElse
import java.util.Comparator
import kotlin.NoSuchElementException


sealed class Heap<out A> {

    internal abstract val comparator: Result<Comparator<@UnsafeVariance A>>

    internal abstract fun left(): Result<Heap<A>>

    internal abstract fun right(): Result<Heap<A>>

    internal abstract fun rank(): Int

    abstract fun head(): Result<A>

    abstract fun length(): Int

    abstract fun isEmpty(): Boolean

    operator fun plus(element: @UnsafeVariance A): Heap<A> = merge(this, Heap(element, comparator))

    abstract fun tail(): Result<Heap<A>>

    abstract fun get(index: Int): Result<A>

    abstract fun pop(): Option<Pair<A, Heap<A>>>

    internal class Empty<out A>(comp: Result<Comparator<@UnsafeVariance A>> = Result.Empty): Heap<A>() {

        override fun pop(): Option<Pair<A, Heap<A>>> = Option()

        override val comparator = comp

        override fun get(index: Int): Result<A> = Result.failure(NoSuchElementException("Index out of bounds"))

        override fun tail(): Result<Heap<A>> = Result.failure(IllegalStateException("tail() called on empty heap"))

        override fun left(): Result<Heap<A>> = Result(Empty(comparator))

        override fun right(): Result<Heap<A>> = Result(Empty(comparator))

        override fun rank(): Int = 0

        override fun head(): Result<Nothing> =
                Result.failure(NoSuchElementException("head() called on empty heap"))

        override fun length(): Int = 0

        override fun isEmpty(): Boolean = true

        override fun toString(): String = "E"
    }

    internal class H<out A>(internal val length: Int,
                            internal val rank: Int,
                            internal val left: Heap<A>,
                            internal val head: A,
                            internal val right: Heap<A>,
                            internal val comp: Result<Comparator<@UnsafeVariance A>> = left.comparator.orElse { right.comparator }): Heap<A>()  {

        override fun pop(): Option<Pair<A, Heap<A>>> = Option(Pair(head, merge(left, right)))

        override val comparator: Result<Comparator<@UnsafeVariance A>> = comp

        override fun get(index: Int): Result<A> = when (index) {
            0 -> Result(head)
            else -> tail().flatMap { it.get(index - 1) }
        }

        override fun tail(): Result<Heap<A>> = Result(merge(left, right))

        override fun left(): Result<Heap<A>> = Result(left)

        override fun right(): Result<Heap<A>> = Result(right)

        override fun rank(): Int = rank

        override fun head(): Result<A> = Result(head)

        override fun length(): Int = length

        override fun isEmpty(): Boolean = false

        override fun toString(): String = "(T $left $head $right)"
    }

    companion object {

        operator fun <A> invoke(): Heap<A> = Empty()

        operator fun <A> invoke(comparator: Comparator<A>): Heap<A> = Empty(Result(comparator))

        operator fun <A> invoke(comparator: Result<Comparator<A>>): Heap<A> = Empty(comparator)

        operator fun <A> invoke(element: A, comparator: Result<Comparator<A>>): Heap<A> =
                H(1, 1, Empty(comparator), element, Empty(comparator), comparator)

        operator fun <A> invoke(element: A, comparator: Comparator<A>): Heap<A> =
                H(1, 1, Empty(Result(comparator)), element, Empty(Result(comparator)), Result(comparator))

        protected fun <A> merge(head: A, first: Heap<A>, second: Heap<A>): Heap<A> {
            val comparator = first.comparator.orElse { second.comparator }
            return when {
                first.rank() >= second.rank() -> H(first.length() + second.length() + 1,
                        second.rank() + 1, first, head, second, comparator)
                else -> H(first.length() + second.length() + 1,
                        first.rank() + 1, second, head, first, comparator)
            }
        }

        fun <A> merge(first: Heap<A>, second: Heap<A>,
                      comparator: Result<Comparator<A>> =
                          first.comparator.orElse { second.comparator }): Heap<A> {
            return first.head().flatMap { fh ->
                second.head().flatMap { sh ->
                    when {
                        compare(fh, sh, comparator) <= 0 -> first.left().flatMap { fl ->
                            first.right().map { fr ->
                                merge(fh, fl, merge(fr, second, comparator))
                            }
                        }
                        else -> second.left().flatMap { sl ->
                            second.right().map { sr ->
                                merge(sh, sl, merge(first, sr, comparator))
                            }
                        }
                    }
                }
            }.getOrElse(when (first) {
                is Empty -> second
                else -> first
            })
        }

        private fun <A> compare(first: A, second: A, comparator: Result<Comparator<A>>): Int =
                comparator.map { comp ->
                    comp.compare(first, second)
                }.getOrElse { (first as Comparable<A>).compareTo(second) }

    }

}