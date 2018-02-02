package com.fpinkotlin.trees.listing01


sealed class Tree<out A: Comparable<@UnsafeVariance A>> { //<1> <2>

    abstract fun isEmpty(): Boolean

    internal object Empty : Tree<Nothing>() { // <3>

        override fun isEmpty(): Boolean = true

        override fun toString(): String = "E"
    }

    internal class T<out A: Comparable<@UnsafeVariance A>>(internal val left: Tree<A>, // <4> <5>
                                                           internal val value: A,
                                                           internal val right: Tree<A>) : Tree<A>() {

        override fun isEmpty(): Boolean = false

        override fun toString(): String = "(T $left $value $right)"
    }

    companion object {

        operator fun <A: Comparable<A>> invoke(): Tree<A> = Empty // <6>
    }
}
