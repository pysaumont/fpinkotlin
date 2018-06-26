package com.fpinkotlin.trees.exercise05

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import kotlin.math.max


sealed class Tree<out A: Comparable<@UnsafeVariance A>> {

    abstract val size: Int

    abstract val height: Int

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

        override fun min(): Result<Nothing> = TODO("min")

        override fun max(): Result<Nothing> = TODO("max")

        override fun isEmpty(): Boolean = true

        override fun toString(): String = "E"
    }

    internal class T<out A: Comparable<@UnsafeVariance A>>(internal val left: Tree<A>,
                                                           internal val value: A,
                                                           internal val right: Tree<A>) : Tree<A>() {

        override val size: Int = 1 + left.size + right.size

        override val height: Int = 1 + max(left.height, right.height)

        override fun min(): Result<A> = TODO("min")


        override fun max(): Result<A> = TODO("max")

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
