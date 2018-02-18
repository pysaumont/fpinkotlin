package com.fpinkotlin.advancedtrees.exercise10

import com.fpinkotlin.advancedtrees.common.List
import com.fpinkotlin.advancedtrees.common.Option
import com.fpinkotlin.advancedtrees.common.Result
import com.fpinkotlin.advancedtrees.common.orElse

sealed class Heap<out A> {

    internal abstract val left: Result<Heap<A>>

    internal abstract val right: Result<Heap<A>>

    protected abstract val rank: Int

    abstract val head: Result<A>

    abstract val size: Int

    abstract val isEmpty: Boolean

    internal abstract val comparator: Result<Comparator<@UnsafeVariance A>>

    abstract fun tail(): Result<Heap<A>>

    abstract fun get(index: Int): Result<A>

    operator fun plus(element: @UnsafeVariance A): Heap<A> = TODO("plus")

    abstract fun pop(): Option<Pair<A, Heap<A>>>

    fun toList(): List<A> = foldLeft(List<A>()) { list -> { a -> list.cons(a) } }.reverse()

    fun <B> foldLeft(identity: B, f: (B) -> (A) -> B): B = unfold(this, { it.pop() }, identity, f)

    private fun <A, S, B> unfold(z: S, getNext: (S) -> Option<Pair<A, S>>, identity: B, f: (B) -> (A) -> B): B {
        tailrec fun unfold(acc: B, z: S): B {
            val next = getNext(z)
            return when (next) {
                Option.None -> acc
                is Option.Some ->
                    unfold(f(acc)(next.value.first), next.value.second)
            }
        }
        return unfold(identity, z)
    }

    internal class Empty<out A>(override val comparator: Result<Comparator<@UnsafeVariance A>> = Result.Empty): Heap<A>() {

        override val isEmpty: Boolean = true

        override val left: Result<Heap<A>> = Result(this)

        override val right: Result<Heap<A>> = Result(this)

        override val head: Result<A> = Result.failure("head() called on empty heap")

        override val rank: Int = 0

        override val size: Int = 0

        override fun tail(): Result<Heap<A>> = Result.failure(IllegalStateException("tail() called on empty heap"))

        override fun get(index: Int): Result<A> = Result.failure(NoSuchElementException("Index out of bounds"))

        override fun pop(): Option<Pair<A, Heap<A>>> = Option()

    }

    internal class H<out A>(override val rank: Int,
                            private val lft: Heap<A>,
                            private val hd: A,
                            private val rght: Heap<A>,
                            override val comparator: Result<Comparator<@UnsafeVariance A>> =
                               lft.comparator.orElse { rght.comparator }): Heap<A>()  {

        override val isEmpty: Boolean = false

        override val left: Result<Heap<A>> = Result(lft)

        override val right: Result<Heap<A>> = Result(rght)

        override val head: Result<A> = Result(hd)

        override val size: Int = lft.size + rght.size + 1

        override fun tail(): Result<Heap<A>> = Result(merge(lft, rght))

        override fun get(index: Int): Result<A> = when (index) {
            0 -> Result(hd)
            else -> tail().flatMap { it.get(index - 1) }
        }

        override fun pop(): Option<Pair<A, Heap<A>>> = Option(Pair(hd, merge(lft, rght)))
    }

    companion object {

        operator fun <A> invoke(): Heap<A> = TODO("invoke (change type as necessary)")

        operator fun <A> invoke(comparator: Comparator<A>): Heap<A> = TODO("invoke")

        operator fun <A> invoke(comparator: Result<Comparator<A>>): Heap<A> = TODO("invoke")

        operator fun <A> invoke(element: A, comparator: Result<Comparator<A>>): Heap<A> =TODO("invoke")

        operator fun <A: Comparable<A>> invoke(element: A): Heap<A> = TODO("invoke")

        operator fun <A> invoke(element: A, comparator: Comparator<A>): Heap<A> = TODO("invoke")

        protected fun <A> merge(head: A, first: Heap<A>, second: Heap<A>): Heap<A> =
            first.comparator.orElse { second.comparator }.let {
                when {
                    first.rank >= second.rank -> H(second.rank + 1, first, head, second, it)
                    else -> H(first.rank + 1, second, head, first, it)
                }
            }

        fun <A> merge(first: Heap<A>, second: Heap<A>,
                      comparator: Result<Comparator<A>> =
                      first.comparator.orElse { second.comparator }): Heap<A> = TODO("merge")

    }
}