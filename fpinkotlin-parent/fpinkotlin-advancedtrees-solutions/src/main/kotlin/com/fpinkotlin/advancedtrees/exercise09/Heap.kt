package com.fpinkotlin.advancedtrees.exercise09

import com.fpinkotlin.advancedtrees.common.List
import com.fpinkotlin.advancedtrees.common.Option
import com.fpinkotlin.advancedtrees.common.Result
import com.fpinkotlin.advancedtrees.common.getOrElse


sealed class Heap<out A: Comparable<@UnsafeVariance A>> {

    internal abstract val left: Result<Heap<A>>

    internal abstract val right: Result<Heap<A>>

    internal abstract val rank: Int

    abstract val head: Result<A>

    abstract val length: Int

    abstract fun isEmpty(): Boolean

    operator fun plus(element: @UnsafeVariance A): Heap<A> = merge(this, Heap(element))

    abstract fun tail(): Result<Heap<A>>

    abstract fun get(index: Int): Result<A>

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

    internal object Empty: Heap<Nothing>() {

        override val left: Result<Heap<Nothing>> by lazy { Result(Empty) }

        override val right: Result<Heap<Nothing>> by lazy { Result(Empty) }

        override val rank: Int = 0

        override val head: Result<Nothing> by lazy { Result.failure<Nothing>("head() called on empty heap") }

        override val length: Int = 0

        override fun pop(): Option<Pair<Nothing, Heap<Nothing>>> = Option()

        override fun get(index: Int): Result<Nothing> = Result.failure(NoSuchElementException("Index out of bounds"))

        override fun tail(): Result<Heap<Nothing>> = Result.failure(IllegalStateException("tail() called on empty heap"))

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

        override fun pop(): Option<Pair<A, Heap<A>>> = Option(Pair(hd, merge(lft, rght)))

        override fun get(index: Int): Result<A> = when (index) {
            0 -> head
            else -> tail().flatMap { it.get(index - 1) }
        }

        override fun tail(): Result<Heap<A>> = Result(merge(lft, rght))

        override fun isEmpty(): Boolean = false
    }

    companion object {

        operator fun <A: Comparable<A>> invoke(): Heap<A> = Empty

        operator fun <A: Comparable<A>> invoke(element: A): Heap<A> = H(1, 1, Empty, element, Empty)

        protected fun <A : Comparable<A>> merge(head: A, first: Heap<A>, second: Heap<A>): Heap<A> =
            when {
                first.rank >= second.rank -> H(first.length + second.length + 1,
                        second.rank + 1, first, head, second)
                else -> H(first.length + second.length + 1,
                        first.rank + 1, second, head, first)
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
                    Empty -> second
                    else -> first
                })
    }

}