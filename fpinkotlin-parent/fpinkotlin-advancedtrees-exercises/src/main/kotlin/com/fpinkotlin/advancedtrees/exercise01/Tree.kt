package com.fpinkotlin.advancedtrees.exercise01

import kotlin.math.max

internal typealias TB<A> = Tree.T.TB<A>
internal typealias TR<A> = Tree.T.TR<A>

sealed class Tree<out A: Comparable<@UnsafeVariance A>> {

    abstract val size: Int

    abstract val height: Int

    operator fun plus(value: @UnsafeVariance A): Tree<A> = TODO("plus")

    private fun <A: Comparable<A>> blacken(t: Tree<A>): Tree<A> = TODO("blacken")

    protected fun add(newVal: @UnsafeVariance A): Tree<A> = TODO("add")

    private fun balance(color: Color, left: Tree<A>, value: A, right: Tree<A>): Tree<A> = TODO("balance")

    internal object E: Tree<Nothing>() {

        internal val color: Color = Tree.Color.Black

        override val size: Int = 0

        override val height: Int = -1

        override fun toString(): String = "E"
    }

    sealed class T<out A: Comparable<@UnsafeVariance A>>(internal val left: Tree<A>,
                                                         internal val value: A,
                                                         internal val right: Tree<A>) : Tree<A>() {

        internal abstract val color: Color

        override val size: Int = left.size + 1 + right.size

        override val height: Int = max(left.height, right.height) + 1

        internal data class TR<out A: Comparable<@UnsafeVariance A>>(internal val l: Tree<A>,
                                                                     internal val v: A,
                                                                     internal val r: Tree<A>) : T<A>(l, v, r) {

            override val color: Color = Tree.Color.Red

            override fun toString(): String = "(T $color $left $value $right)"
        }

        internal data class TB<out A: Comparable<@UnsafeVariance A>>(internal val l: Tree<A>,
                                                                     internal val v: A,
                                                                     internal val r: Tree<A>) : T<A>(l, v, r) {
            override val color: Color = Tree.Color.Black

            override fun toString(): String = "(T $color $left $value $right)"
        }

    }

    companion object {

        operator fun <A: Comparable<A>> invoke(): Tree<A> = E

    }

    sealed class Color {

        internal object Black : Color() {

            override fun toString() = "B"
        }

        internal object Red : Color() {

            override fun toString() = "R"
        }
    }
}
