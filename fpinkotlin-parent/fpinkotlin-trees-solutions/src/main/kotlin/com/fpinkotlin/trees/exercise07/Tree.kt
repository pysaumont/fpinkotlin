package com.fpinkotlin.trees.exercise07

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.common.orElse
import kotlin.math.max


sealed class Tree<out A: Comparable<@UnsafeVariance A>> {

    abstract fun merge(tree: Tree<@UnsafeVariance A>): Tree<A>

    abstract fun min(): Result<A>

    abstract fun max(): Result<A>

    abstract fun size(): Int

    abstract fun height(): Int

    abstract fun isEmpty(): Boolean

    operator fun plus(a: @UnsafeVariance A): Tree<A> = plus(this, a)

    fun remove(a: @UnsafeVariance A): Tree<A> = when(this) {
        is Tree.Empty -> this
        is Tree.T     ->  when {
            a < value -> T(left.remove(a), value, right)
            a > value -> T(left, value, right.remove(a))
            else -> left.removeMerge(right)
        }
    }

    fun removeMerge(ta: Tree<@UnsafeVariance A>): Tree<A> = when (this) {
        is Tree.Empty -> ta
        is Tree.T     -> when (ta) {
            is Empty -> this
            is T -> when {
                ta.value < value -> T(left.removeMerge(ta), value, right)
                ta.value > value -> T(left, value, right.removeMerge(ta))
                else             -> throw IllegalStateException("We shouldn't be here")

            }
        }
    }

    fun contains(a: @UnsafeVariance A): Boolean = when (this) {
        is Empty -> false
        is T -> when {
            a < value -> left.contains(a)
            a > value -> right.contains(a)
            else -> value == a
        }
    }

    internal object Empty : Tree<Nothing>() {

        override fun merge(tree: Tree<Nothing>): Tree<Nothing> = tree

        override fun min(): Result<Nothing> = Result()

        override fun max(): Result<Nothing> = Result()

        override fun size(): Int = 0

        override fun height(): Int = -1

        override fun isEmpty(): Boolean = true

        override fun toString(): String = "E"
    }

    internal class T<out A: Comparable<@UnsafeVariance A>>(internal val left: Tree<A>,
                                                           internal val value: A,
                                                           internal val right: Tree<A>) : Tree<A>() {

        override fun merge(tree: Tree<@UnsafeVariance A>): Tree<A> = when (tree) {
            is Empty -> this
            is T ->   when  {
                tree.value > this.value -> T(left, value, right.merge(T(Empty, tree.value, tree.right))).merge(tree.left)
                tree.value < this.value -> T(left.merge(T(tree.left, tree.value, Empty)), value, right).merge(tree.right)
                else                    -> T(left.merge(tree.left), value, right.merge(tree.right))
            }
        }

        override fun min(): Result<A> = left.min().orElse { Result(value) }

        override fun max(): Result<A> = right.max().orElse { Result(value) }

        override fun size(): Int = 1 + left.size() + right.size()

        override fun height(): Int = 1 + max(left.height(), right.height())

        override fun isEmpty(): Boolean = false

        override fun toString(): String = "(T $left $value $right)"
    }

    companion object {

        fun <A: Comparable<A>> plus(tree: Tree<A>, a: A): Tree<A> {
            return when(tree) {
                is Empty -> T(tree, a, tree)
                is T -> {
                    when {
                        a < tree.value -> Tree.T(plus(tree.left, a), tree.value, tree.right)
                        a > tree.value -> Tree.T(tree.left, tree.value, plus(tree.right, a))
                        else -> tree
                    }
                }
            }
        }

        operator fun <A: Comparable<A>> invoke(): Tree<A> = Empty

        operator fun <A: Comparable<A>> invoke(vararg az: A): Tree<A> =
            az.foldRight(Empty, { a: A, tree: Tree<A> -> tree.plus(a) })

        operator fun <A: Comparable<A>> invoke(list: List<A>): Tree<A> =
            list.foldLeft(Empty as Tree<A>, { tree: Tree<A> -> { a: A -> tree.plus(a) } })
    }
}
