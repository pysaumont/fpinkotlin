package com.fpinkotlin.advancedTreeOlds.exercise02

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.common.getOrElse
import kotlin.math.max

internal typealias TB<A> = TreeOld.T.TB<A>
internal typealias TR<A> = TreeOld.T.TR<A>
internal typealias TNB<A> = TreeOld.T.TNB<A>
internal typealias TBB<A> = TreeOld.T.TBB<A>

/*
 * see http://www.cs.cmu.edu/~rwh/theses/okasaki.pdf
 * see http://matt.might.net/papers/germane2014deletion.pdf
 * see http://matt.might.net/articles/red-black-delete/
 */
sealed class TreeOld<out A: Comparable<@UnsafeVariance A>> {

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

    private fun <A: Comparable<A>> blacken(t: TreeOld<A>): TreeOld<A> = when (t) {
        is Empty -> E
        is T     -> T.TB(t.left, t.value, t.right)
    }

    private fun redden(t: TreeOld<A>): TreeOld<A> = when (t) {
        is Empty -> throw IllegalStateException("Empty TreeOlds may not be reddened")
        is T     -> TR(t.left, t.value, t.right)
    }

    internal fun redder(): TreeOld<A> = when (this) {
        is Empty -> E
        is T -> when (color.redder()) {
            TreeOld.Color.Black         -> TB(left, value, right)
            TreeOld.Color.Red           -> TR(left, value, right)
            TreeOld.Color.DoubleBlack   -> throw IllegalStateException("redder function will never result into DoubleBlack")
            TreeOld.Color.NegativeBlack -> TNB(left, value, right)
        }
    }

    operator fun plus(element: @UnsafeVariance A): TreeOld<A> {
        return blacken(add(element))
    }

    internal fun add(element: @UnsafeVariance A): TreeOld<A> {

        return when (this) {
            is T     ->  when {
                element < value -> balance(color, left.add(element), value, right)
                element > value -> balance(color, left, value, right.add(element))
                else           -> when (color) {
                    TreeOld.Color.Black         -> TB(left, element, right)
                    TreeOld.Color.Red           -> TR(left, element, right)
                    TreeOld.Color.NegativeBlack -> TNB(left, element, right)
                    TreeOld.Color.DoubleBlack   -> throw IllegalStateException("There is no such thing as a DoubleBlack TreeOld")
                }
            }
            is Empty -> TR(E, element, E)
        }
    }

    operator fun minus(elem: @UnsafeVariance A): TreeOld<A> {
        return blacken(delete(elem))
    }

    internal fun delete(elem: @UnsafeVariance A): TreeOld<A> = when (this) {
        is Empty -> E
        is T -> when {
            elem < this.value -> bubble(this.color, this.left.delete(elem), this.value, this.right)
            elem > this.value -> bubble(this.color, this.left, this.value, this.right.delete(elem))
            else               -> remove()
        }
    }

    protected fun removeMax(): TreeOld<A> = when (this) {
        is Empty -> throw IllegalStateException("removeMax called on Empty")
        is T -> when (right) {
            is Empty -> remove()
            is T -> bubble(color, left, value, right.removeMax())
        }
    }

    protected fun balance(color: Color,
                          left: TreeOld<@UnsafeVariance A>,
                          value: @UnsafeVariance A,
                          right: TreeOld<@UnsafeVariance A>): TreeOld<A> = when (color) {
        TreeOld.Color.Black       -> {
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
        TreeOld.Color.DoubleBlack -> {
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
                       balance(TreeOld.Color.Black, right.left.right, right.value, redden(right.right)))
            // balance BB (T NB a@(T B _ _ _) x (T B b y c)) z d = T B (balance B (redden a) x b) y (T B c z d)
                left is TNB && left.left is TB && left.right is TB ->
                    TB(balance(TreeOld.Color.Black, redden(left.left), left.value, left.right.left), left.right.value,
                       TB(left.right.right, value, right))
            // (T color a x b) = (T color a x b)
                else -> TB(left, value, right)
            }
        }
    // (T color a x b) = (T color a x b)
        else                    -> when (color) {
            TreeOld.Color.Black         -> TB(left, value, right)
            TreeOld.Color.Red           -> TR(left, value, right)
            TreeOld.Color.DoubleBlack   -> TBB(left, value, right)
            TreeOld.Color.NegativeBlack -> TNB(left, value, right)
        }
    }

    internal abstract class Empty: TreeOld<Nothing>() {

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

        override val color: Color = TreeOld.Color.Black

        override fun toString(): String = "E"
    }

    internal object EE: Empty() {

        override val color: Color = TreeOld.Color.DoubleBlack

        override fun toString(): String = "EE"
    }

    sealed class T<out A: Comparable<@UnsafeVariance A>>(internal val left: TreeOld<A>,
                                                         internal val value: A,
                                                         internal val right: TreeOld<A>) : TreeOld<A>() {

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

//        override fun toString(): String = toString(this)

        fun <A: Comparable<A>> toString(t: TreeOld<A>): String {
            val tableHeight = t.height + 1
            val tableWidth = Math.pow(2.0, (t.height + 1).toDouble()).toInt() - 1
            val table: Array<Array<String>> = Array(tableHeight) { Array(tableWidth) { "" } }
            val hPosition = tableWidth / 2
            val vPosition = t.height
            val result = makeTable(table, t, hPosition, vPosition)
            val sb = StringBuilder()
            for (l in result.size downTo 1) {
                for (c in 0 until result[0].size) {
                    sb.append(makeCell(result[l - 1][c]))
                }
                sb.append("\n")
            }
            return sb.toString()
        }

        private fun makeCell(s: String?): String = when (s) {
            null -> "    "
            else -> when (s.length) {
                0    -> "    "
                1    -> " $s  "
                2    -> " $s "
                3    -> s + " "
                else -> s
            }
        }

        private fun <A: Comparable<A>> makeTable(table: Array<Array<String>>,
                                                 t: TreeOld<A>,
                                                 hPosition: Int,
                                                 vPosition: Int): Array<Array<String>> =
            when (t) {
                 is Empty -> table
                 is T     -> {
                     val shift = Math.pow(2.0, (t.height - 1).toDouble()).toInt()
                     val lhPosition = hPosition - shift
                     val rhPosition = hPosition + shift
                     table[vPosition][hPosition] = "${t.color}${t.value}"
                     val t2 = makeTable<A>(table, t.left, lhPosition, vPosition - 1)
                     makeTable(t2, t.right, rhPosition, vPosition - 1)
                 }
             }

//        internal fun remove(): TreeOld<A> {
//            return when {
//                this is TR && left == E && right is Empty -> E
//                this is TB && left == E && right is Empty -> EE
//                this is TB && left == E && right is TR -> TB(right.left, right.value, right.right)
//                this is TB && left is TR && right is Empty -> TB(left.left, left.value, left.right)
//                else -> when {
//                    left is Empty && right is T -> bubble(right.color, right.left, right.value, right.right)
//                    else -> bubble(color, left.removeMax(), left.max(), right)
//                }
//            }
//        }

        internal fun remove(): TreeOld<A> {
            println(this.javaClass)
            if (color == TreeOld.Color.Red && left.isEmpty() && right.isEmpty()) {
                return E
            }
            if ((color == TreeOld.Color.Black || color == TreeOld.Color.DoubleBlack) && left.isEmpty() && right.isEmpty()) {
                return EE
            }
            if ((color == TreeOld.Color.Black || color == TreeOld.Color.DoubleBlack)  && left.isEmpty() && right is T && right.color == TreeOld.Color.Red) {
                return TB(right.left, right.value, right.right)
            }
            if ((color == TreeOld.Color.Black || color == TreeOld.Color.DoubleBlack) && left is T && left.color == TreeOld.Color.Red &&
                right.isEmpty()) {
                return TB(left.left, left.value, left.right)
            }
            return if (left.isEmpty()) {
                (right as T)
                bubble(right.color, right.left, right.value, right.right)
            } else bubble(color, left.removeMax(), left.max(), right)
        }

        internal fun bubble(color: Color,
                            left: TreeOld<@UnsafeVariance A>,
                            elem: @UnsafeVariance A,
                            right: TreeOld<@UnsafeVariance A>): TreeOld<A> =
            when {
                left.color == TreeOld.Color.DoubleBlack ||
                    right.color == TreeOld.Color.DoubleBlack ->
                    balance(color.blacker(), left.redder(), elem, right.redder())
                else                                       -> balance(color, left, elem, right)
            }

        internal class TR<out A: Comparable<@UnsafeVariance A>>(l: TreeOld<A>,
                                                                internal val v: A,
                                                                r: TreeOld<A>) : T<A>(l, v, r) {
            override val color: Color = TreeOld.Color.Red
        }

        internal class TB<out A: Comparable<@UnsafeVariance A>>(l: TreeOld<A>,
                                                                internal val v: A,
                                                                r: TreeOld<A>) : T<A>(l, v, r) {
            override val color: Color = TreeOld.Color.Black
        }

        internal class TNB<out A: Comparable<@UnsafeVariance A>>(l: TreeOld<A>,
                                                                 internal val v: A,
                                                                 r: TreeOld<A>) : T<A>(l, v, r) {
            override val color: Color = TreeOld.Color.NegativeBlack
        }

        internal class TBB<out A: Comparable<@UnsafeVariance A>>(l: TreeOld<A>,
                                                                 internal val v: A,
                                                                 r: TreeOld<A>) : T<A>(l, v, r) {
            override val color: Color = TreeOld.Color.DoubleBlack
        }
    }

    companion object {

        operator fun <A: Comparable<A>> invoke(): TreeOld<A> = E

        fun <A> unfold(a: A, f: (A) -> Result<A>): A {
            tailrec fun <A> unfold(a: Pair<Result<A>, Result<A>>,
                                   f: (A) -> Result<A>): Pair<Result<A>, Result<A>> {
                val x = a.second.flatMap { f(it) }
                return when {
                    x.map { true }.getOrElse(false) -> unfold(Pair(a.second, x), f) // x = Result.success
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
