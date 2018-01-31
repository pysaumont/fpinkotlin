package com.fpinkotlin.advancedtrees.exercise01

import kotlin.math.max

internal typealias TB<A> = Old.T.TB<A>
internal typealias TR<A> = Old.T.TR<A>

sealed class Old<out A: Comparable<@UnsafeVariance A>> {

    abstract val size: Int

    abstract val height: Int

    operator fun plus(value: @UnsafeVariance A): Old<A> = blacken(add(value))

    private fun <A: Comparable<A>> blacken(tree: Old<A>): Old<A> = when (tree) {
        E -> E
        is T -> T.TB(tree.left, tree.value, tree.right)
    }

    protected fun add(newVal: @UnsafeVariance A): Old<A> = when (this) {
        E -> TR(E, newVal, E)
        is T ->  when {
            newVal < value -> balance(color, left.add(newVal), value, right)
            newVal > value -> balance(color, left, value, right.add(newVal))
            else           -> when (color) {
                Old.Color.Black -> TB(left, newVal, right)
                Old.Color.Red   -> TR(left, newVal, right)
            }
        }
    }

    private fun balance(color: Color, left: Old<A>, value: A, right: Old<A>): Old<A> =
            when (color) {
                Old.Color.Black -> {
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
                Old.Color.Red   -> TR(left, value, right)
            }

    internal object E: Old<Nothing>() {

        internal val color: Color = Old.Color.Black

        override val size: Int = 0

        override val height: Int = -1

        override fun toString(): String = "E"
    }

    sealed class T<out A: Comparable<@UnsafeVariance A>>(internal val left: Old<A>,
                                                         internal val value: A,
                                                         internal val right: Old<A>) : Old<A>() {

        internal abstract val color: Color

        override val size: Int = left.size + 1 + right.size

        override val height: Int = max(left.height, right.height) + 1

        internal data class TR<out A: Comparable<@UnsafeVariance A>>(private val l: Old<A>,
                                                                     internal val v: A,
                                                                     private val r: Old<A>) : T<A>(l, v, r) {

            override val color: Color = Old.Color.Red

            override fun toString(): String = "(T $color $left $value $right)"
        }

        internal data class TB<out A: Comparable<@UnsafeVariance A>>(internal val l: Old<A>,
                                                                     internal val v: A,
                                                                     internal val r: Old<A>) : T<A>(l, v, r) {
            override val color: Color = Old.Color.Black

            override fun toString(): String = "(T $color $left $value $right)"
        }

    }

    companion object {

        operator fun <A: Comparable<A>> invoke(): Old<A> = E

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
