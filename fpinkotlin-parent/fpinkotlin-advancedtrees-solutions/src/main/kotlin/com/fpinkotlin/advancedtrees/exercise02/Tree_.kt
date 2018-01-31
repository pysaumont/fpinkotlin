package com.fpinkotlin.advancedTree_s.exercise02

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.common.getOrElse
import kotlin.math.max

internal typealias TB<A> = Tree_.T.TB<A>
internal typealias TR<A> = Tree_.T.TR<A>
internal typealias TNB<A> = Tree_.T.TNB<A>
internal typealias TBB<A> = Tree_.T.TBB<A>

/*
 * see http://www.cs.cmu.edu/~rwh/theses/okasaki.pdf
 * see http://matt.might.net/papers/germane2014deletion.pdf
 * see http://matt.might.net/articles/red-black-delete/
 */
sealed class Tree_<out A: Comparable<@UnsafeVariance A>> {

    abstract val size: Int

    abstract val height: Int

    internal abstract val left: Tree_<A>

    internal abstract val value: A

    internal abstract val right: Tree_<A>

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

    abstract fun pathLengths(currentDepth: Int, depths: List<Int>): List<Int>

    abstract fun pathColors(currentColorList: List<Color>, paths: List<List<Color>>): List<List<Color>>

    internal abstract fun isE(): Boolean

    internal abstract fun isT(): Boolean

    internal abstract fun isB(): Boolean

    internal abstract fun isBB(): Boolean

    internal abstract fun isTB(): Boolean

    internal abstract fun isTR(): Boolean

    internal abstract fun isTNB(): Boolean

    fun pathLengths(): List<Int> = pathLengths(0, List())

    fun pathColors(): List<List<Color>> = pathColors(List(), List())

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

    private fun <A: Comparable<A>> blacken(t: Tree_<A>): Tree_<A> = when (t) {
        is Empty -> E
        is T     -> TB(t.left, t.value, t.right)
    }

    private fun redden(t: Tree_<A>): Tree_<A> = when (t) {
        is Empty -> throw IllegalStateException("Empty Tree_s may not be reddened")
        is T     -> TR(t.left, t.value, t.right)
    }

    internal fun redder(): Tree_<A> = when (this) {
        is Empty -> E
        is T -> when (color.redder()) {
            Tree_.Color.Black -> TB(left, value, right)
            Tree_.Color.NegativeBlack -> TNB(left, value, right)
            Tree_.Color.Red -> TR(left, value, right)
            Tree_.Color.DoubleBlack -> throw IllegalStateException("redder function will never result into DoubleBlack")
        }
    }

    operator fun plus(value: @UnsafeVariance A): Tree_<A> {
        return blacken(add(value))
    }

    internal fun add(newVal: @UnsafeVariance A): Tree_<A> {

        return when (this) {
            is T     ->  when {
                newVal < value -> balance(color, left.add(newVal), value, right)
                newVal > value -> balance(color, left, value, right.add(newVal))
                else           -> when (color) {
                    Tree_.Color.Black -> TB(left, newVal, right)
                    Tree_.Color.Red -> TR(left, newVal, right)
                    Tree_.Color.NegativeBlack -> TNB(left, newVal, right)
                    Tree_.Color.DoubleBlack -> throw IllegalStateException("There is no such thing as a DoubleBlack Tree_")
                }
            }
            is Empty -> TR(E, newVal, E)
        }
    }

    operator fun minus(elem: @UnsafeVariance A): Tree_<A> {
        return blacken(delete(elem))
    }

//    internal fun delete(elem: @UnsafeVariance A): Tree_<A> = when (this) {
//        is Empty -> this
//        is T -> when {
//            elem < this.value -> bubble(this.color, this.left.delete(elem), this.value, this.right)
//            elem > this.value -> bubble(this.color, this.left, this.value, this.right.delete(elem))
//            else              -> remove()
//        }
//    }

    internal fun delete(value:  @UnsafeVariance A): Tree_<A> = when (this) {
        is Empty -> E
        is T -> if (value.compareTo(this.value) < 0)
            bubble(this.color, this.left.delete(value), this.value, this.right)
        else if (value.compareTo(this.value) > 0)
            bubble(this.color, this.left, this.value, this.right.delete(value))
        else
            remove()
    }

    //    protected fun removeMax(): Tree_<A> = when (this) {
//        is Empty -> throw IllegalStateException("removeMax called on Empty")
//        is T -> when (right) {
//            is Empty -> remove()
//            is T -> bubble(color, left, value, right.removeMax())
//        }
//    }
    protected fun removeMax(): Tree_<A> {
        when (this) {
            is Empty ->  throw IllegalStateException("removeMax called on Empty")
            is T -> return if (right.isEmpty()) {
                remove()
            } else bubble(color, left, value, right.removeMax())
        }
    }

//    protected fun balance(color: Color,
//                          left: Tree_<@UnsafeVariance A>,
//                          value: @UnsafeVariance A,
//                          right: Tree_<@UnsafeVariance A>): Tree_<A> = when (color) {
//        Tree_.Color.Black -> {
//            when {
//            // (T B (T R (T R a x b) y c) z d) = (T R (T B a x b ) y (T B c z d))
//                left.isTR() && (left).left.isTR() ->
//                    TR(blacken(left.left), left.value, TB(left.right, value, right))
//            // (T B (T R a x (T R b y c)) z d) = (T R (T B a x b) y (T B c z d))
//                left.isTR() && left.right.isTR() ->
//                    TR(TB(left.left, left.value, left.right.left),
//                            left.right.value, TB(left.right.right, value, right))
//            // (T B a x (T R (T R b y c) z d)) = (T R (T B a x b) y (T B c z d))
//                right.isTR() && right.left.isTR() ->
//                    TR(TB(left, value, right.left.left), right.left.value,
//                            TB(right.left.right, right.value, right.right))
//            // (T B a x (T R b y (T R c z d))) = (T R (T B a x b) y (T B c z d))
//                right.isTR() && right.right.isTR() ->
//                    TR(TB(left, value, right.left), right.value,
//                            blacken(right.right))
//            // (T color a x b) = (T color a x b)
//                else -> TB(left, value, right)
//            }
//        }
//    // following patterns are added for removal of an element
//        Tree_.Color.DoubleBlack -> {
//            when {
//            // balance BB (T R (T R a x b) y c) z d = T B (T B a x b) y (T B c z d)
//                left.isTR() && left.left.isTR() ->
//                    TB(blacken(left.left), left.value, TB(left.right, value, right))
//            // balance BB (T R a x (T R b y c)) z d = T B (T B a x b) y (T B c z d)
//                left.isTR() && left.right.isTR() ->
//                    TB(TB(left.left, left.value, left.right.left), left.right.value,
//                            TB(left.right.right, value, right))
//            // balance BB a x (T R (T R b y c) z d) = T B (T B a x b) y (T B c z d)
//                right.isTR() && right.left.isTR() ->
//                    TB(TB(left, value, right.left.left), right.left.value,
//                            TB(right.left.right, right.value, right.right))
//            // balance BB a x (T R b y (T R c z d)) = T B (T B a x b) y (T B c z d)
//                right.isTR() && right.right.isTR() ->
//                    TB(TB(left, value, right.left), right.value, blacken(right.right))
//            // balance BB a x (T NB (T B b y c) z d@(T B _ _ _)) = T B (T B a x b) y (balance B c z (redden d))
//                right.isTNB() && right.left.isTB() && right.right.isTB() ->
//                    TB(TB(left, value, right.left.left), right.left.value,
//                            balance(Tree_.Color.Black, right.left.right, right.value, redden(right.right)))
//            // balance BB (T NB a@(T B _ _ _) x (T B b y c)) z d = T B (balance B (redden a) x b) y (T B c z d)
//                left.isTNB() && left.left.isTB() && left.right.isTB() ->
//                    TB(balance(Tree_.Color.Black, redden(left.left), left.value, left.right.left), left.right.value,
//                            TB(left.right.right, value, right))
//            // (T color a x b) = (T color a x b)
//                else -> TB(left, value, right)
//            }
//        }
//    // (T color a x b) = (T color a x b)
//        else -> TR(left, value, right)
//    }
    internal fun balance(color: Color, left: Tree_<@UnsafeVariance A>, value: @UnsafeVariance A, right: Tree_<@UnsafeVariance A>): Tree_<A> {
        // balance B (T R (T R a x b) y c) z d = T R (T B a x b) y (T B c z d)
        if (color.isB() && left.isTR() && left.left.isTR()) {
            return TR(blacken(left.left), left.value, TB(left.right, value, right))
        }
        // balance B (T R a x (T R b y c)) z d = T R (T B a x b) y (T B c z d)
        if (color.isB() && left.isTR() && left.right.isTR()) {
            return TR(TB(left.left, left.value, left.right.left), left.right.value,
                        TB(left.right.right, value, right))
        }
        // balance B a x (T R (T R b y c) z d) = T R (T B a x b) y (T B c z d)
        if (color.isB() && right.isTR() && right.left.isTR()) {
            return TR(TB(left, value, right.left.left), right.left.value,
                        TB(right.left.right, right.value, right.right))
        }
        // balance B a x (T R b y (T R c z d)) = T R (T B a x b) y (T B c z d)
        if (color.isB() && right.isTR() && right.right.isTR()) {
            return TR(TB(left, value, right.left), right.value, blacken(right.right))
        }

        // balance BB (T R (T R a x b) y c) z d = T B (T B a x b) y (T B c z d)
        if (color.isBB() && left.isTR() && left.left.isTR()) {
            return TB(blacken(left.left), left.value, TB(left.right, value, right))
        }
        // balance BB (T R a x (T R b y c)) z d = T B (T B a x b) y (T B c z d)
        if (color.isBB() && left.isTR() && left.right.isTR()) {
            return TB(TB(left.left, left.value, left.right.left), left.right.value,
                        TB(left.right.right, value, right))
        }
        // balance BB a x (T R (T R b y c) z d) = T B (T B a x b) y (T B c z d)
        if (color.isBB() && right.isTR() && right.left.isTR()) {
            return TB(TB(left, value, right.left.left), right.left.value,
                        TB(right.left.right, right.value, right.right))
        }
        // balance BB a x (T R b y (T R c z d)) = T B (T B a x b) y (T B c z d)
        if (color.isBB() && right.isTR() && right.right.isTR()) {
            return TB(TB(left, value, right.left), right.value, blacken(right.right))
        }
        // balance BB a x (T NB (T B b y c) z d@(T B _ _ _)) = T B (T B a x b) y (balance B c z (redden d))
        if (color.isBB() && right.isTNB() && right.left.isTB() && right.right.isTB()) {
            return TB(TB(left, value, right.left.left), right.left.value,
                        balance(Tree_.Color.Black, right.left.right, right.value, redden(right.right)))
        }
        // balance BB (T NB a@(T B _ _ _) x (T B b y c)) z d = T B (balance B (redden a) x b) y (T B c z d)
        return if (color.isBB() && left.isTNB() && left.left.isTB() && left.right.isTB()) {
            TB(balance(Tree_.Color.Black, redden(left.left), left.value, left.right.left), left.right.value,
                 TB(left.right.right, value, right))
        }
        else when (color) {
            Tree_.Color.Black -> TB(left, value, right)
            Tree_.Color.Red -> TB(left, value, right)
            Tree_.Color.DoubleBlack -> TBB(left, value, right)
            Tree_.Color.NegativeBlack -> TNB(left, value, right)
        }

        // balance color a x b = T color a x b
    }

    internal abstract class Empty: Tree_<Nothing>() {

        override val left: Tree_<Nothing> by lazy { throw IllegalStateException("Empty has no left branch") }

        override val value: Nothing by lazy { throw IllegalStateException("Empty has no value") }

        override val right: Tree_<Nothing> by lazy { throw IllegalStateException("Empty has no right branch") }

        override fun isT(): Boolean = false

        override fun isTB(): Boolean = false

        override fun isTR(): Boolean = false

        override fun isTNB(): Boolean = false

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

        override fun pathLengths(currentDepth: Int, depths: List<Int>): List<Int> = List()

        override fun pathColors(currentColorList: List<Color>, paths: List<List<Color>>): List<List<Color>> =
                paths.cons(currentColorList)
    }

    internal object E: Empty() {

        override fun isE(): Boolean = true

        override fun isB(): Boolean = true

        override fun isBB(): Boolean = false

        override val color: Color = Tree_.Color.Black

        override fun toString(): String = "E"
    }

    internal object EE: Empty() {

        override fun isE(): Boolean = true

        override fun isB(): Boolean = false

        override fun isBB(): Boolean = true

        override val color: Color = Tree_.Color.DoubleBlack

        override fun toString(): String = "EE"
    }

    sealed class T<out A: Comparable<@UnsafeVariance A>>(override val left: Tree_<A>,
                                                         override val value: A,
                                                         override val right: Tree_<A>) : Tree_<A>() {

        override fun isE(): Boolean = false

        override fun isT(): Boolean = true

        override fun pathLengths(currentDepth: Int, depths: List<Int>): List<Int> =
                when {
                    right.isEmpty() && left.isEmpty() -> depths.cons(currentDepth)
                    else -> List.concat(left.pathLengths(currentDepth + 1, depths), right.pathLengths(currentDepth + 1, depths))
                }

        override fun pathColors(currentColorList: List<Color>, paths: List<List<Color>>): List<List<Color>> =
                when {
                    right.isEmpty() && left.isEmpty() -> paths.cons(currentColorList.cons(color))
                    else -> List.concat(left.pathColors(currentColorList.cons(color), paths), right.pathColors(currentColorList.cons(color), paths))
                }

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

//        internal fun remove(): Tree_<A> {
//            println(this.color)
//            return when {
//                isTR() && left.isEmpty() && right.isEmpty() -> E
//                isTB() && left.isEmpty() && right.isEmpty() -> EE
//                isTB() && left.isEmpty() && right.isTR() -> TB(right.left, right.value, right.right)
//                isTB() && left.isTR() && right.isEmpty() -> TB(left.left, left.value, left.right)
//                left.isEmpty() && right.isT() -> bubble(right.color, right.left, right.value, right.right)
//                else -> bubble(color, left.removeMax(), left.max(), right)
//            }
//        }

        internal fun remove(): Tree_<A> {
            println(color)
            if (isTR() && left.isEmpty() && right.isEmpty()) {
                return E
            }
            if (isTB() && left.isEmpty() && right.isEmpty()) {
                return EE
            }
            if (isTB() && left.isEmpty() && right.isTR()) {
                return TB(right.left, right.value, right.right)
            }
            if (isTB() && left.isTR() && right.isEmpty()) {
                return TB(left.left, left.value, left.right)
            }
            return if (left.isEmpty()) {
                bubble(right.color, right.left, right.value, right.right)
            } else bubble(color, left.removeMax(), left.max(), right)
        }

//        internal fun bubble(color: Color,
//                            left: Tree_<@UnsafeVariance A>,
//                            elem: @UnsafeVariance A,
//                            right: Tree_<@UnsafeVariance A>): Tree_<A> =
//                when {
//                    left.color == Tree_.Color.DoubleBlack ||
//                            right.color == Tree_.Color.DoubleBlack ->
//                        balance(color.blacker(), left.redder(), elem, right.redder())
//                    else -> balance(color, left, elem, right)
//                }
        internal fun bubble(color: Color, left: Tree_<@UnsafeVariance A>, value: @UnsafeVariance A, right: Tree_<@UnsafeVariance A>): Tree_<A> {
            return if (left.isBB() || right.isBB())
                balance(color.blacker(), left.redder(), value, right.redder())
            else
                balance(color, left, value, right)
        }

        internal class TR<out A: Comparable<@UnsafeVariance A>>(l: Tree_<A>,
                                                                internal val v: A,
                                                                r: Tree_<A>) : T<A>(l, v, r) {

            override fun isB(): Boolean = this.color.isB()

            override fun isBB(): Boolean = this.color.isBB()

            override fun isTB(): Boolean = this.color.isB() || this.color.isBB()

            override fun isTR(): Boolean = this.color.isR()

            override fun isTNB(): Boolean = this.color.isNB()

            override val color: Color = Tree_.Color.Red

        }

        internal class TB<out A: Comparable<@UnsafeVariance A>>(l: Tree_<A>,
                                                                internal val v: A,
                                                                r: Tree_<A>) : T<A>(l, v, r) {

            override fun isB(): Boolean = this.color.isB()

            override fun isBB(): Boolean = this.color.isBB()

            override fun isTB(): Boolean = this.color.isB() || this.color.isBB()

            override fun isTR(): Boolean = this.color.isR()

            override fun isTNB(): Boolean = this.color.isNB()

            override val color: Color = Tree_.Color.Black
        }

        internal class TNB<out A: Comparable<@UnsafeVariance A>>(l: Tree_<A>,
                                                                 internal val v: A,
                                                                 r: Tree_<A>) : T<A>(l, v, r) {

            override fun isB(): Boolean = this.color.isB()

            override fun isBB(): Boolean = this.color.isBB()

            override fun isTB(): Boolean = this.color.isB() || this.color.isBB()

            override fun isTR(): Boolean = this.color.isR()

            override fun isTNB(): Boolean = this.color.isNB()

            override val color: Color = Tree_.Color.NegativeBlack
        }

        internal class TBB<out A: Comparable<@UnsafeVariance A>>(l: Tree_<A>,
                                                                 internal val v: A,
                                                                 r: Tree_<A>) : T<A>(l, v, r) {

            override fun isB(): Boolean = this.color.isB()

            override fun isBB(): Boolean = this.color.isBB()

            override fun isTB(): Boolean = this.color.isB() || this.color.isBB()

            override fun isTR(): Boolean = this.color.isR()

            override fun isTNB(): Boolean = this.color.isNB()

            override val color: Color = Tree_.Color.DoubleBlack
        }
    }

    companion object {

        operator fun <A: Comparable<A>> invoke(): Tree_<A> = E

        fun <A> unfold(a: A, f: (A) -> Result<A>): A {
            tailrec fun <A> unfold(a: Pair<Result<A>, Result<A>>,
                                   f: (A) -> Result<A>): Pair<Result<A>, Result<A>> {
                val x = a.second.flatMap { f(it) }
                return when {
                    x.map { true }.getOrElse(false) -> unfold(Pair(a.second, x), f)
                    else -> a
                }
            }
            return Result(a).let { unfold(Pair(it, it), f).second.getOrElse(a) }
        }
    }

    sealed class Color {

        internal abstract fun blacker(): Color

        internal abstract fun redder(): Color

        internal abstract fun isR(): Boolean

        internal abstract fun isB(): Boolean

        internal abstract fun isBB(): Boolean

        internal abstract fun isNB(): Boolean

        internal object Black : Color() {

            override fun isR(): Boolean = false

            override fun isB(): Boolean = true

            override fun isBB(): Boolean = false

            override fun isNB(): Boolean = false

            override fun blacker(): Color = DoubleBlack

            override fun redder(): Color = Red

            override fun toString() = "B"
        }

        internal object DoubleBlack : Color() {

            override fun isR(): Boolean = false

            override fun isB(): Boolean = false

            override fun isBB(): Boolean = true

            override fun isNB(): Boolean = false

            override fun blacker(): Color = throw IllegalStateException("Can't make DoubleBlack blacker")

            override fun redder(): Color = Black

            override fun toString() = "BB"
        }

        internal object NegativeBlack : Color() {

            override fun isR(): Boolean = false

            override fun isB(): Boolean = false

            override fun isBB(): Boolean = false

            override fun isNB(): Boolean = true

            override fun blacker(): Color = Red

            override fun redder(): Color = throw IllegalStateException("Can't make NegativeBlack redder")

            override fun toString() = "NB"
        }

        internal object Red : Color() {

            override fun isR(): Boolean = true

            override fun isB(): Boolean = false

            override fun isBB(): Boolean = false

            override fun isNB(): Boolean = false


            override fun blacker(): Color = Black

            override fun redder(): Color = NegativeBlack

            override fun toString() = "R"
        }
    }

}
