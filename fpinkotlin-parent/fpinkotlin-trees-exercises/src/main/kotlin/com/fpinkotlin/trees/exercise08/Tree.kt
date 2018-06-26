package com.fpinkotlin.trees.exercise08

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import kotlin.math.max


sealed class Tree<out A: Comparable<@UnsafeVariance A>> {

    abstract val size: Int

    abstract val height: Int

    abstract fun <B> foldLeft(identity: B,
                              f: (B) -> (A) -> B,
                              g: (B) -> (B) -> B): B

    abstract fun merge(tree: Tree<@UnsafeVariance A>): Tree<A>

    abstract fun min(): Result<A>

    abstract fun max(): Result<A>

    abstract fun isEmpty(): Boolean

    operator fun plus(a: @UnsafeVariance A): Tree<A> = when (this) {
        Empty -> T(Empty, a, Empty)
        is T -> when {
            a < this.value -> T(left + a, this.value, right)
            a > this.value -> T(left, this.value, right + a)
            else -> T(this.left, a, this.right)
        }
    }

    fun remove(a: @UnsafeVariance A): Tree<A> = when(this) {
        Empty -> this
        is T  ->  when {
            a < value -> T(left.remove(a), value, right)
            a > value -> T(left, value, right.remove(a))
            else -> left.removeMerge(right)
        }
    }

    fun removeMerge(ta: Tree<@UnsafeVariance A>): Tree<A> = when (this) {
        Empty -> ta
        is T  -> when (ta) {
            Empty -> this
            is T -> when {
                ta.value < value -> T(left.removeMerge(ta), value, right)
                ta.value > value -> T(left, value, right.removeMerge(ta))
                else             -> throw IllegalStateException("We shouldn't be here")
            }
        }
    }

    fun contains(a: @UnsafeVariance A): Boolean = when (this) {
        Empty -> false
        is T -> when {
            a < value -> left.contains(a)
            a > value -> right.contains(a)
            else -> value == a
        }
    }

    internal object Empty : Tree<Nothing>() {

        override val size: Int = 0

        override val height: Int = -1

        override fun <B> foldLeft(identity: B, f: (B) -> (Nothing) -> B, g: (B) -> (B) -> B): B = TODO("foldLeft")

        override fun merge(tree: Tree<Nothing>): Tree<Nothing> = tree

        override fun min(): Result<Nothing> = Result()

        override fun max(): Result<Nothing> = Result()

        override fun isEmpty(): Boolean = true

        override fun toString(): String = "E"
    }

    internal class T<out A: Comparable<@UnsafeVariance A>>(internal val left: Tree<A>,
                                                           internal val value: A,
                                                           internal val right: Tree<A>) : Tree<A>() {

        override val size: Int = 1 + left.size + right.size

        override val height: Int = 1 + max(left.height, right.height)

        override fun <B> foldLeft(identity: B, f: (B) -> (A) -> B, g: (B) -> (B) -> B): B = TODO("foldLeft")

        override fun merge(tree: Tree<@UnsafeVariance A>): Tree<A> = when (tree) {
            Empty -> this
            is T ->   when  {
                tree.value > this.value -> T(left, value, right.merge(T(Empty, tree.value, tree.right))).merge(tree.left)
                tree.value < this.value -> T(left.merge(T(tree.left, tree.value, Empty)), value, right).merge(tree.right)
                else                    -> T(left.merge(tree.left), value, right.merge(tree.right))
            }
        }

        override fun min(): Result<A> = left.min().orElse { Result(value) }

        override fun max(): Result<A> = right.max().orElse { Result(value) }

        override fun isEmpty(): Boolean = false

        override fun toString(): String = "(T $left $value $right)"
    }

    companion object {

        operator fun <A: Comparable<A>> invoke(): Tree<A> = Empty

        operator fun <A: Comparable<A>> invoke(vararg az: A): Tree<A> =
            az.fold(Empty) { tree: Tree<A>, a: A -> tree.plus(a) }

        operator fun <A: Comparable<A>> invoke(list: List<A>): Tree<A> =
            list.foldLeft(Empty as Tree<A>) { tree: Tree<A> -> { a: A -> tree.plus(a) } }
    }
}
