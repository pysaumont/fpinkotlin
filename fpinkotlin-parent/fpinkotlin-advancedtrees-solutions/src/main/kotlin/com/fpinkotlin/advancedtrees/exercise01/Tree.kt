package com.fpinkotlin.advancedtrees.exercise01

import kotlin.math.max

internal typealias TB<A> = Tree.T.TB<A>
internal typealias TR<A> = Tree.T.TR<A>
internal typealias Black = Tree.Color.Black
internal typealias Red = Tree.Color.Red

sealed class Tree<out A: Comparable<@UnsafeVariance A>> {

    abstract fun size(): Int
    abstract fun height(): Int

    protected fun add(newVal: @UnsafeVariance A): Tree<A> {

        return when (this) {
            is T ->  when {
                newVal < value      -> balance(color, left.add(newVal), value, right)
                newVal > value -> balance(color, left, value, right.add(newVal))
                else                -> when (color) {
                    is Black -> TB(left, newVal, right)
                    is Red -> TR(left, newVal, right)
                }
            }
            is E -> TR(E, newVal, E)
        }
    }

    private fun <A: Comparable<A>> blacken(t: Tree<A>): Tree<A> = when (t) {
        is E -> E
        is T -> T.TB(t.left, t.value, t.right)
    }

    operator fun plus(value: @UnsafeVariance A): Tree<A> {
        return blacken(add(value))
    }

    private fun balance(color: Color, left: Tree<A>, value: A, right: Tree<A>): Tree<A> = when (color) {
        is Black -> {
            when {
            // (T B (T R (T R a x b) y c) z d) = (T R (T B a x b ) y (T B c z d))
                left is TR && (left).left is TR ->
                    TR(TB((left.left as T).left, left.left.value, left.left.right),
                            left.value, TB(left.right, value, right))
            // (T B (T R a x (T R b y c)) z d) = (T R (T B a x b) y (T B c z d))
                left is TR && left.right is TR ->
                    TR(TB(left.left, left.value, left.right.left),
                            left.right.value, TB(left.right.right, value, right))
            // (T B a x (T R (T R b y c) z d)) = (T R (T B a x b) y (T B c z d))
                right is TR && right.left is TR ->
                    TR(TB(left, value, right.left.left), right.left.value,
                            TB(right.left.right, right.value, right.right))
            // (T B a x (T R b y (T R c z d))) = (T R (T B a x b) y (T B c z d))
                right is TR && right.right is TR ->
                    TR(TB(left, value, right.left), right.value,
                            TB(right.right.left, right.right.value, right.right.right))
            // (T color a x b) = (T color a x b)
                else -> TB(left, value, right)
            }
        }
    // (T color a x b) = (T color a x b)
        is Red -> TR(left, value, right)
    }


    internal object E: Tree<Nothing>() {

        override fun size(): Int = 0

        override fun height(): Int = -1

        override fun toString(): String = "E"
    }

    sealed class T<out A: Comparable<@UnsafeVariance A>>(internal val left: Tree<A>,
                                                         internal val value: A,
                                                         internal val right: Tree<A>) : Tree<A>() {
        internal abstract val color: Color

        override fun size(): Int = left.size() + 1 + right.size()

        override fun height(): Int = max(left.height(), right.height()) + 1

        override fun toString(): String = "(T $color $left $value $right)"

        internal data class TR<out A: Comparable<@UnsafeVariance A>>(internal val l: Tree<A>,
                                                                     internal val v: A,
                                                                     internal val r: Tree<A>) : T<A>(l, v, r) {
            override val color: Color = Red
        }

        internal data class TB<out A: Comparable<@UnsafeVariance A>>(internal val l: Tree<A>,
                                                                     internal val v: A,
                                                                     internal val r: Tree<A>) : T<A>(l, v, r) {
            override val color: Color = Black
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

fun log2nlz(n: Int): Int {
    return if (n == 0)
        0
    else
        31 - Integer.numberOfLeadingZeros(n)
}

fun main(args: Array<String>) {
    val tree = Tree<Int>() + 3 + 2 + 1
    println(tree)
}