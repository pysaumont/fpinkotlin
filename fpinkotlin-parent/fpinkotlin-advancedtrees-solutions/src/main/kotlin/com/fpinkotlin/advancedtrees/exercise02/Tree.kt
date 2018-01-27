package com.fpinkotlin.advancedtrees.exercise02

import com.fpinkotlin.advancedtrees.common.List
import com.fpinkotlin.advancedtrees.common.Result
import com.fpinkotlin.advancedtrees.common.getOrElse
import kotlin.math.max

internal typealias TB<A> = Tree.T.TB<A>
internal typealias TR<A> = Tree.T.TR<A>
internal typealias TNB<A> = Tree.T.TNB<A>

/*
 * see http://www.cs.cmu.edu/~rwh/theses/okasaki.pdf
 * see http://matt.might.net/papers/germane2014deletion.pdf
 * see http://matt.might.net/articles/red-black-delete/
 */
sealed class Tree<out A: Comparable<@UnsafeVariance A>> {

    abstract val size: Int

    abstract val height: Int

    internal abstract val color: Color

    abstract fun isEmpty(): Boolean

    abstract fun max(): A

    abstract fun min(): A

    abstract fun <B> foldInOrder(identity: B, f: (B) -> (A) -> (B) -> B): B

    abstract fun <B> foldInReverseOrder(identity: B, f: (B) -> (A) -> (B) -> B): B

    abstract fun <B> foldPreOrder(identity: B, f: (A) -> (B) -> (B) -> B): B

    abstract fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (A) -> B): B

    abstract fun <B> foldLeft(identity: B,
                              f: (B) -> (A) -> B,
                              g: (B) -> (B) -> B): B

    abstract fun toListPreOrderLeft(): List<A>

    abstract fun <B> foldRight(identity: B,
                               f: (A) -> (B) -> B,
                               g: (B) -> (B) -> B): B

    abstract operator fun get(element: @UnsafeVariance A): Result<A>

    fun <B> foldLeft(identity: B, f: (B) -> (A) -> B): B =
            toListPreOrderLeft().foldLeft(identity, f)

    fun contains(a: @UnsafeVariance A): Boolean = when (this) {
        is Empty -> false
        is T -> when {
            a < value -> left.contains(a)
            a > value -> right.contains(a)
            else -> value == a
        }
    }

    private fun <A: Comparable<A>> blacken(t: Tree<A>): Tree<A> = when (t) {
        is Empty -> E
        is T     -> T.TB(t.left, t.value, t.right)
    }

    private fun redden(t: Tree<A>): Tree<A> = when (t) {
        is Empty -> throw IllegalStateException("Empty trees may not be reddened")
        is T     -> TR(t.left, t.value, t.right)
    }

    internal fun redder(): Tree<A> = when (this) {
        is Empty -> E
        is T -> when (color.redder()) {
            Tree.Color.Black -> TB(left, value, right)
            Tree.Color.NegativeBlack -> TNB(left, value, right)
            Tree.Color.Red -> TR(left, value, right)
            Tree.Color.DoubleBlack -> throw IllegalStateException("redder function will never result into DoubleBlack")
        }
    }

    operator fun plus(value: @UnsafeVariance A): Tree<A> {
        return blacken(add(value))
    }

    internal fun add(newVal: @UnsafeVariance A): Tree<A> {

        return when (this) {
            is T     ->  when {
                newVal < value -> balance(color, left.add(newVal), value, right)
                newVal > value -> balance(color, left, value, right.add(newVal))
                else           -> when (color) {
                    Tree.Color.Black -> TB(left, newVal, right)
                    Tree.Color.Red -> TR(left, newVal, right)
                    Tree.Color.NegativeBlack -> TNB(left, newVal, right)
                    Tree.Color.DoubleBlack -> throw IllegalStateException("There is no such thing as a DoubleBlack tree")
                }
            }
            is Empty -> TR(E, newVal, E)
        }
    }

    operator fun minus(elem: @UnsafeVariance A): Tree<A> {
        return blacken(delete(elem))
    }

    internal fun delete(elem: @UnsafeVariance A): Tree<A> = when (this) {
        is Empty -> this
        is T -> when {
            elem < this.value -> bubble(this.color, this.left.delete(elem), this.value, this.right)
            elem > this.value -> bubble(this.color, this.left, this.value, this.right.delete(elem))
            else               -> remove()
        }
    }

    protected fun removeMax(): Tree<A> = when (this) {
        is Empty -> throw IllegalStateException("removeMax called on Empty")
        is T -> when (right) {
            is Empty -> remove()
            is T -> bubble(color, left, value, right.removeMax())
        }
    }

    protected fun balance(color: Color,
                          left: Tree<@UnsafeVariance A>,
                          value: @UnsafeVariance A,
                          right: Tree<@UnsafeVariance A>): Tree<A> = when (color) {
        Tree.Color.Black -> {
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
    // following patterns are added for removal of an element
        Tree.Color.DoubleBlack -> {
            when {
            // balance BB (T R (T R a x b) y c) z d = T B (T B a x b) y (T B c z d)
                left is TR && left.left is TR ->
                    TB(blacken(left.left), left.value, TB(left.right, value, right))
            // balance BB (T R a x (T R b y c)) z d = T B (T B a x b) y (T B c z d)
                left is TR && left.right is TR ->
                    TB(TB(left.left, left.value, left.right.left), left.right.value,
                            TB(left.right.right, value, right))
            // balance BB a x (T R (T R b y c) z d) = T B (T B a x b) y (T B c z d)
                right is TR && right.left is TR ->
                    TB(TB(left, value, right.left.left), right.left.value,
                            TB(right.left.right, right.value, right.right))
            // balance BB a x (T R b y (T R c z d)) = T B (T B a x b) y (T B c z d)
                right is TR && right.right is TR ->
                    TB(TB(left, value, right.left), right.value, blacken(right.right))
            // balance BB a x (T NB (T B b y c) z d@(T B _ _ _)) = T B (T B a x b) y (balance B c z (redden d))
                right is TNB && right.left is TB && right.right is TB ->
                    TB(TB(left, value, right.left.left), right.left.value,
                            balance(Tree.Color.Black, right.left.right, right.value, redden(right.right)))
            // balance BB (T NB a@(T B _ _ _) x (T B b y c)) z d = T B (balance B (redden a) x b) y (T B c z d)
                left is TNB && left.left is TB && left.right is TB ->
                    TB(balance(Tree.Color.Black, redden(left.left), left.value, left.right.left), left.right.value,
                            TB(left.right.right, value, right))
            // (T color a x b) = (T color a x b)
                else -> TB(left, value, right)
            }
        }
    // (T color a x b) = (T color a x b)
        else -> TR(left, value, right)
    }

    internal abstract class Empty: Tree<Nothing>() {

        override fun isEmpty(): Boolean = true

        override val size: Int = 0

        override val height: Int = -1

        override fun max(): Nothing = throw IllegalStateException("max called on Empty")

        override fun min(): Nothing = throw IllegalStateException("min called on Empty")

        override fun toListPreOrderLeft(): List<Nothing> = List()

        override fun <B> foldInOrder(identity: B, f: (B) -> (Nothing) -> (B) -> B): B = identity

        override fun <B> foldInReverseOrder(identity: B, f: (B) -> (Nothing) -> (B) -> B): B = identity

        override fun <B> foldPreOrder(identity: B, f: (Nothing) -> (B) -> (B) -> B): B = identity

        override fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (Nothing) -> B): B = identity

        override fun <B> foldRight(identity: B, f: (Nothing) -> (B) -> B, g: (B) -> (B) -> B): B = identity

        override fun <B> foldLeft(identity: B, f: (B) -> (Nothing) -> B, g: (B) -> (B) -> B): B = identity

        override fun get(element: Nothing): Result<Nothing> = Result()
    }

    internal object E: Empty() {

        override val color: Color = Tree.Color.Black

        override fun toString(): String = "E"
    }

    internal object EE: Empty() {

        override val color: Color = Tree.Color.DoubleBlack

        override fun toString(): String = "EE"
    }

    sealed class T<out A: Comparable<@UnsafeVariance A>>(internal val left: Tree<A>,
                                                         internal val value: A,
                                                         internal val right: Tree<A>) : Tree<A>() {

        override fun isEmpty(): Boolean = false

        override val size: Int = left.size + 1 + right.size

        override val height: Int = max(left.height, right.height) + 1

        override fun max(): A = when (right) {
            is Empty -> value
            else -> right.max()
        }

        override fun min(): A = when (left) {
            is Empty -> value
            else -> left.min()
        }

        override fun get(element: @UnsafeVariance A): Result<A> = when {
            value < this.value -> left[value]
            value > this.value -> right[value]
            else -> Result(this.value)
        }

        override fun toListPreOrderLeft(): List<A> =
                left.toListPreOrderLeft().concat(right.toListPreOrderLeft()).cons(value)

        override fun <B> foldInOrder(identity: B, f: (B) -> (A) -> (B) -> B): B =
                f(left.foldInOrder(identity, f))(value)(right.foldInOrder(identity, f))

        override fun <B> foldInReverseOrder(identity: B, f: (B) -> (A) -> (B) -> B): B =
                f(right.foldInReverseOrder(identity, f))(value)(left.foldInReverseOrder(identity, f))

        override fun <B> foldPreOrder(identity: B, f: (A) -> (B) -> (B) -> B): B =
                f(value)(left.foldPreOrder(identity, f))(right.foldPreOrder(identity, f))

        override fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (A) -> B): B =
                f(left.foldPostOrder(identity, f))(right.foldPostOrder(identity, f))(value)

        override fun <B> foldLeft(identity: B, f: (B) -> (A) -> B, g: (B) -> (B) -> B): B =
                g(right.foldLeft(identity, f, g))(f(left.foldLeft(identity, f, g))(this.value))

        override fun <B> foldRight(identity: B, f: (A) -> (B) -> B, g: (B) -> (B) -> B): B =
                g(f(this.value)(left.foldRight(identity, f, g)))(right.foldRight(identity, f, g))

        override fun toString(): String = "(T $color $left $value $right)"

        internal fun remove(): Tree<A> {
            return when {
                this is TR && left == E && right is Empty -> E
                this is TB && left == E && right is Empty -> EE
                this is TB && left == E && right is TR -> TB(right.left, right.value, right.right)
                this is TB && left is TR && right is Empty -> TB(left.left, left.value, left.right)
                else -> when {
                    left is Empty && right is T -> bubble(right.color, right.left, right.value, right.right)
                    else -> bubble(color, left.removeMax(), left.max(), right)
                }
            }
        }

        internal fun bubble(color: Color,
                            left: Tree<@UnsafeVariance A>,
                            elem: @UnsafeVariance A,
                            right: Tree<@UnsafeVariance A>): Tree<A> =
                when {
                    left.color == Tree.Color.DoubleBlack ||
                            right.color == Tree.Color.DoubleBlack ->
                        balance(color.blacker(), left.redder(), elem, right.redder())
                    else -> balance(color, left, elem, right)
                }

        internal class TR<out A: Comparable<@UnsafeVariance A>>(l: Tree<A>,
                                                                internal val v: A,
                                                                r: Tree<A>) : T<A>(l, v, r) {

            override val color: Color = Tree.Color.Red

        }

        internal class TB<out A: Comparable<@UnsafeVariance A>>(l: Tree<A>,
                                                                internal val v: A,
                                                                r: Tree<A>) : T<A>(l, v, r) {
            override val color: Color = Tree.Color.Black
        }

        internal class TNB<out A: Comparable<@UnsafeVariance A>>(l: Tree<A>,
                                                                 internal val v: A,
                                                                 r: Tree<A>) : T<A>(l, v, r) {
            override val color: Color = Tree.Color.NegativeBlack
        }
    }

    companion object {

        operator fun <A: Comparable<A>> invoke(): Tree<A> = E

        fun <A> unfold(a: A, f: (A) -> Result<A>): A {
            tailrec fun <A> unfold(a: Pair<Result<A>, Result<A>>,
                                   f: (A) -> Result<A>): Pair<Result<A>, Result<A>> {
                val x = a.second.flatMap { f(it) }
                return when (x) {
                    is Result.Success -> unfold(Pair(a.second, x), f)
                    else -> a
                }
            }
            return Result(a).let { unfold(Pair(it, it), f).second.getOrElse(a) }
        }
    }

    sealed class Color {

        abstract fun blacker(): Color

        abstract fun redder(): Color

        internal object Black : Color() {

            override fun blacker(): Color = DoubleBlack

            override fun redder(): Color = Red

            override fun toString() = "B"
        }

        internal object DoubleBlack : Color() {

            override fun blacker(): Color = throw IllegalStateException("Can't make DoubleBlack blacker")

            override fun redder(): Color = Black

            override fun toString() = "BB"
        }

        internal object NegativeBlack : Color() {

            override fun blacker(): Color = Red

            override fun redder(): Color = throw IllegalStateException("Can't make NegativeBlack redder")

            override fun toString() = "NB"
        }

        internal object Red : Color() {

            override fun blacker(): Color = Black

            override fun redder(): Color = NegativeBlack

            override fun toString() = "R"
        }
    }

}
