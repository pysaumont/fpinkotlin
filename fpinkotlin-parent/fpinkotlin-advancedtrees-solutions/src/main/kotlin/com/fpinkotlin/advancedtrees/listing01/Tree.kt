package com.fpinkotlin.advancedtrees.listing01


import com.fpinkotlin.advancedtrees.listing01.Tree.Color.B
import com.fpinkotlin.advancedtrees.listing01.Tree.Color.R
import kotlin.math.max

sealed class Tree<out A: Comparable<@UnsafeVariance A>> {

    abstract val size: Int

    abstract val height: Int // <2> Abstract properties are defined in the parent class

    internal abstract val color: Color

    internal abstract val isTB: Boolean

    internal abstract val isTR: Boolean

    internal abstract val right: Tree<A>

    internal abstract val left: Tree<A>

    internal abstract val value: A

    internal abstract class Empty<out A: Comparable<@UnsafeVariance A>>: Tree<A>() { // <3> Empty is an abstract class, which will
        // allow
        // implementing functions in in this class instead of using pattern matching in the Tree parent class

        override val isTB: Boolean = false

        override val isTR: Boolean = false

        override val right: Tree<Nothing> by lazy { // <4> Properties that make no sense in the Empty class are lazily initialised.
            throw IllegalStateException("right called on Empty tree")
        }

        override val left: Tree<Nothing> by lazy {
            throw IllegalStateException("left called on Empty tree")
        }

        override val value: Nothing by lazy {
            throw IllegalStateException("value called on Empty tree")
        }

        override val color: Color = B

        override val size: Int = 0

        override val height: Int = -1

        override fun toString(): String = "E"
    }

    internal object E: Empty<Nothing>() // <5> The empty tree is represented by the E singleton

    internal class T<out A: Comparable<@UnsafeVariance A>>(override val color: Color,
                                                           override val left: Tree<A>,
                                                           override val value: A,
                                                           override val right: Tree<A>) : Tree<A>() {
        override val isTB: Boolean = color == B

        override val isTR: Boolean = color == R

        override val size: Int = left.size + 1 + right.size

        override val height: Int = max(left.height, right.height) + 1

        override fun toString(): String = "(T $color $left $value $right)"
    }

    companion object {

        operator fun <A: Comparable<A>> invoke(): Tree<A> = E

    }

    sealed class Color { // <5> These are the colors used for coloring the nodes

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
