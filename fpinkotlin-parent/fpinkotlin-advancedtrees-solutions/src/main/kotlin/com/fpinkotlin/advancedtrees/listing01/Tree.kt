package com.fpinkotlin.advancedtrees.listing01


import kotlin.math.max

internal typealias TB<A> = Tree.T.TB<A> // <1> These type alias will simplify the code
internal typealias TR<A> = Tree.T.TR<A>
internal typealias Black = Tree.Color.Black
internal typealias Red = Tree.Color.Red

sealed class Tree<out A: Comparable<@UnsafeVariance A>> {

    abstract fun size(): Int

    abstract fun height(): Int

    internal object E: Tree<Nothing>() {

        internal val color: Color = Black // <2> The empty tree is Black

        override fun size(): Int = 0

        override fun height(): Int = -1

        override fun toString(): String = "E"
    }

    sealed class T<out A: Comparable<@UnsafeVariance A>>(internal val left: Tree<A>, // <3> T is the parent class of non empty trees
                                                         internal val value: A,
                                                         internal val right: Tree<A>) : Tree<A>() {

        internal abstract val color: Color // <4> color is an abstract property that will be overridden in extending classes

        override fun size(): Int = left.size() + 1 + right.size()

        override fun height(): Int = max(left.height(), right.height()) + 1

        internal data class TR<out A: Comparable<@UnsafeVariance A>>(internal val l: Tree<A>, // <5> TR represents a red non empty tree
                                                                     internal val v: A,
                                                                     internal val r: Tree<A>) : T<A>(l, v, r) {

            override val color: Color = Red

            override fun toString(): String = "(T $color $left $value $right)"
        }

        internal data class TB<out A: Comparable<@UnsafeVariance A>>(internal val l: Tree<A>, // <6> TB represents a black non empty tree
                                                                     internal val v: A,
                                                                     internal val r: Tree<A>) : T<A>(l, v, r) {
            override val color: Color = Black

            override fun toString(): String = "(T $color $left $value $right)"
        }

    }

    companion object {

        operator fun <A: Comparable<A>> invoke(): Tree<A> = E // <7> This function creates an emtpy tree

    }

    sealed class Color {

        internal object Black : Color() { // <8> Colors are singleton objects.

            override fun toString() = "B"
        }

        internal object Red : Color() {

            override fun toString() = "R"
        }
    }
}
