package com.fpinkotlin.advancedtrees.exercise01

import com.fpinkotlin.advancedtrees.exercise01.Tree.Color.B
import com.fpinkotlin.advancedtrees.exercise01.Tree.Color.R
import kotlin.math.max

/*
 * see http://www.cs.cmu.edu/~rwh/theses/okasaki.pdf
 */
sealed class Tree<out A: Comparable<@UnsafeVariance A>> {

    abstract val size: Int

    abstract val height: Int

    abstract val color: Color

    internal abstract val isTB: Boolean

    internal abstract val isTR: Boolean

    internal abstract val right: Tree<A>

    internal abstract val left: Tree<A>

    internal abstract val value: A

    operator fun plus(value: @UnsafeVariance A): Tree<A> = TODO("plus")

    protected abstract fun blacken(): Tree<A>

    protected abstract fun add(newVal: @UnsafeVariance A): Tree<A>

    protected fun balance(color: Color,
                          left: Tree<@UnsafeVariance A>,
                          value: @UnsafeVariance A,
                          right: Tree<@UnsafeVariance A>): Tree<A> = TODO("balance")

    internal abstract class Empty<out A: Comparable<@UnsafeVariance A>>: Tree<A>() {

        override val isTB: Boolean = false

        override val isTR: Boolean = false

        override val right: Tree<A> by lazy { throw IllegalStateException("right called on Empty tree") }

        override val left: Tree<A> by lazy { throw IllegalStateException("left called on Empty tree") }

        override val value: A by lazy { throw IllegalStateException("value called on Empty tree") }

        override val color: Color = B

        override val size: Int = 0

        override val height: Int = -1

        override fun blacken(): Tree<A> = TODO("blacken")

        override fun add(newVal: @UnsafeVariance A): Tree<A> = TODO("add")

        override fun toString(): String = "E"
    }

    internal object E: Empty<Nothing>()

    internal class T<out A: Comparable<@UnsafeVariance A>>(override val color: Color,
                                                           override val left: Tree<A>,
                                                           override val value: A,
                                                           override val right: Tree<A>) : Tree<A>() {
        override val isTB: Boolean = color == B

        override val isTR: Boolean = color == R

        override val size: Int = left.size + 1 + right.size

        override val height: Int = max(left.height, right.height) + 1

        override fun blacken(): Tree<A> = TODO("blacken")

        override fun add(newVal: @UnsafeVariance A): Tree<A> = TODO("add")

        override fun toString(): String = "(T $color $left $value $right)"
    }

    companion object {

        operator fun <A: Comparable<A>> invoke(): Tree<A> = E

    }

    sealed class Color {

        // Red
        internal object R: Color() {
            override fun toString(): String = "R"
        }

        // Black
        internal object B: Color() {
            override fun toString(): String = "B"
        }
    }
}
