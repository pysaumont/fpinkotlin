package com.fpinkotlin.advancedtrees.exercise04

import com.fpinkotlin.advancedtrees.exercise04.Tree.Color.B
import com.fpinkotlin.advancedtrees.exercise04.Tree.Color.BB
import com.fpinkotlin.advancedtrees.exercise04.Tree.Color.NB
import com.fpinkotlin.advancedtrees.exercise04.Tree.Color.R
import com.fpinkotlin.common.List
import com.fpinkotlin.common.List.Companion.concat
import com.fpinkotlin.common.List.Companion.cons
import com.fpinkotlin.common.Result


/*
 * see http://www.cs.cmu.edu/~rwh/theses/okasaki.pdf
 * see http://matt.might.net/papers/germane2014deletion.pdf
 * see http://matt.might.net/articles/red-black-delete/
 */
sealed class Tree<out A: Comparable<@UnsafeVariance A>> {

    abstract val size: Int
    abstract val height: Int
    abstract val isEmpty: Boolean

    internal abstract val isB: Boolean
    internal abstract val isBB: Boolean
    internal abstract val isTB: Boolean
    internal abstract val isTR: Boolean
    internal abstract val isTNB: Boolean

    internal abstract val right: Tree<A>
    internal abstract val left: Tree<A>
    internal abstract val value: A
    internal abstract val color: Color

    private val g: (List<A>) -> (List<A>) -> List<A> = { a -> { b -> concat(a, b) } }
    private val f: (List<A>) -> (A) -> List<A> = { l -> { a ->  cons(a, l) } }

    abstract fun contains(element: @UnsafeVariance A): Boolean
    abstract fun max(): A
    abstract fun min(): A
    protected abstract fun removeMax(): Tree<A>
    abstract fun pathLengths(currentDepth: Int, depths: List<Int>): List<Int>
    protected abstract fun pathColors(currentColorList: List<Color>, paths: List<List<Color>>): List<List<Color>>

    fun pathLengths(): List<Int> = pathLengths(0, List())
    protected fun pathColors(): List<List<Color>> = pathColors(List(), List())

    abstract operator fun get(element: @UnsafeVariance A): Result<A>
    protected abstract fun getT(element: @UnsafeVariance A): Result<T<@UnsafeVariance A>>

    abstract fun <B> foldLeft(identity: B, f: (B) -> (A) -> B, g: (B) -> (B) -> B): B
    abstract fun <B> foldRight(identity: B, f: (A) -> (B) -> B, g: (B) -> (B) -> B): B
    abstract fun <B> foldInOrder(identity: B, f: (B) -> (A) -> (B) -> B): B
    abstract fun <B> foldInReverseOrder(identity: B, f: (B) -> (A) -> (B) -> B): B
    abstract fun <B> foldPreOrder(identity: B, f: (A) -> (B) -> (B) -> B): B
    abstract fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (A) -> B): B

    internal abstract fun redder(): Tree<A>

    internal abstract fun add(element: @UnsafeVariance A): Tree<A>

    operator fun plus(element: @UnsafeVariance A): Tree<A> = add(element).blacken()

    operator fun minus(element: @UnsafeVariance A): Tree<A> = delete(element).blacken()

    internal abstract fun delete(element: @UnsafeVariance A): Tree<A>

    protected abstract fun blacken(): Tree<A>

    protected abstract fun redden(): Tree<A>

    fun toList(): List<A> = foldLeft(List(), f, g)

    private abstract class Empty<A: Comparable<A>>: Tree<A>() {

        override val size: Int = 0

        override val height: Int = -1

        override val right: Tree<A> = E

        override val left: Tree<A> = E

        override val value: A by lazy { throw IllegalStateException("value called on Empty") }

        override val isTB: Boolean = false

        override val isTR: Boolean = false

        override val isEmpty: Boolean = true

        override val isTNB: Boolean = false

        override fun blacken(): Tree<A> = E

        override fun redden(): Tree<A> = throw IllegalStateException("Empty trees may not be reddened")

        override fun removeMax(): Tree<A> = throw IllegalStateException("removeMax called on Empty")

        override fun max(): A = throw IllegalStateException("max called on Empty")

        override fun min(): A = throw IllegalStateException("min called on Empty")

        override fun contains(element: A): Boolean = false

        override fun pathLengths(currentDepth: Int, depths: List<Int>): List<Int> = List()

        override fun get(element: A): Result<A> = Result()

        override fun getT(element: A): Result<T<A>> = Result()

        override fun <B> foldLeft(identity: B, f: (B) -> (A) -> B, g: (B) -> (B) -> B): B = identity

        override fun <B> foldRight(identity: B, f: (A) -> (B) -> B, g: (B) -> (B) -> B): B = identity

        override fun <B> foldInOrder(identity: B, f: (B) -> (A) -> (B) -> B): B = identity

        override fun <B> foldInReverseOrder(identity: B, f: (B) -> (A) -> (B) -> B): B = identity

        override fun <B> foldPreOrder(identity: B, f: (A) -> (B) -> (B) -> B): B = identity

        override fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (A) -> B): B = identity

        override fun add(element: A): Tree<A> = T(R, E, element, E)

        override fun delete(element: A): Tree<A> = E

        override fun pathColors(currentColorList: List<Color>, paths: List<List<Color>>): List<List<Color>> =
            paths.cons(currentColorList)
    }

    private object E: Empty<Nothing>() {

        override val color: Color = R

        override val isB: Boolean = true

        override val isBB: Boolean = false

        override fun redder(): Tree<Nothing> = this

        override fun toString(): String = "E"
    }

    private object EE: Empty<Nothing>() {

        override val color: Color = BB

        override val isB: Boolean = false

        override val isBB: Boolean = true

        override fun redder(): Tree<Nothing> = E

        override fun toString(): String = "EE"
    }

    protected class T<A: Comparable<A>> internal constructor(override val color: Color,
                                                             override val left: Tree<A>,
                                                             override val value: A,
                                                             override val right: Tree<A>): Tree<A>() {

        override val size: Int = left.size + 1 + right.size

        override val height: Int = Math.max(left.height, right.height) + 1

        override val isB: Boolean = color == B

        override val isBB: Boolean = color == BB

        override val isTB: Boolean = color == B || color == BB

        override val isTR: Boolean = color == R

        override val isTNB: Boolean = color == NB

        override val isEmpty: Boolean = false

        override fun blacken(): Tree<A> = T(B, left, value, right)

        override fun redden(): Tree<A> = T(R, left, value, right)

        private fun balance(color: Color, left: Tree<A>, value: A, right: Tree<A>): Tree<A> = when {

        // balance B (T R (T R a x b) y c) z d = T R (T B a x b) y (T B c z d)
            color == B && left.isTR && left.left.isTR ->
                T(R, left.left.blacken(), left.value, T(B, left.right, value,right))

        // balance B (T R a x (T R b y c)) z d = T R (T B a x b) y (T B c z d)
            color == B && left.isTR && left.right.isTR ->
                T(R, T(B, left.left, left.value, left.right.left), left.right.value,
                  T(B, left.right.right, value, right))

        // balance B a x (T R (T R b y c) z d) = T R (T B a x b) y (T B c z d)
            color == B && right.isTR && right.left.isTR ->
                T(R, T(B, left, value, right.left.left), right.left.value,
                  T(B, right.left.right, right.value, right.right))

        // balance B a x (T R b y (T R c z d)) = T R (T B a x b) y (T B c z d)
            color == B && right.isTR && right.right.isTR ->
                T(R, T(B, left, value, right.left), right.value, right.right.blacken())

        // balance BB (T R (T R a x b) y c) z d = T B (T B a x b) y (T B c z d)
            color == BB && left.isTR && left.left.isTR ->
                T(B, left.left.blacken(), left.value, T(B, left.right, value, right))

        // balance BB (T R a x (T R b y c)) z d = T B (T B a x b) y (T B c z d)
            color == BB && left.isTR && left.right.isTR ->
                T(B, T(B, left.left, left.value, left.right.left), left.right.value,
                  T(B, left.right.right, value, right))

        // balance BB a x (T R (T R b y c) z d) = T B (T B a x b) y (T B c z d)
            color == BB && right.isTR && right.left.isTR ->
                T(B, T(B, left, value, right.left.left), right.left.value,
                  T(B, right.left.right, right.value, right.right))

        // balance BB a x (T R b y (T R c z d)) = T B (T B a x b) y (T B c z d)
            color == BB && right.isTR && right.right.isTR ->
                T(B, T(B, left, value, right.left), right.value, right.right.blacken())

        // balance BB a x (T NB (T B b y c) z d@(T B _ _ _)) = T B (T B a x b) y (balance B c z (redden d))
            color == BB && right.isTNB && right.left.isTB && right.right.isTB ->
                T(B, T(B, left, value, right.left.left), right.left.value,
                  balance(B, right.left.right, right.value, right.right.redden()))

        // balance BB (T NB a@(T B _ _ _) x (T B b y c)) z d = T B (balance B (redden a) x b) y (T B c z d)
            color == BB && left.isTNB && left.left.isTB && left.right.isTB ->
                T(B, balance(B, left.left.redden(), left.value, left.right.left), left.right.value,
                  T(B, left.right.right, value, right))

        // balance color a x b = T color a x b
            else -> T(color, left, value, right)
        }

        private fun bubble(color: Color, left: Tree<A>, value: A, right: Tree<A>): Tree<A> = when {
            left.isBB || right.isBB -> balance(color.blacker, left.redder(), value, right.redder())
            else                    -> balance(color, left, value, right)
        }

        override fun contains(element: A): Boolean = when {
            element < this.value -> left.contains(element)
            else                 -> element <= this.value || right.contains(element)
        }

        override fun max(): A = when {
            right.isEmpty -> value
            else          -> right.max()
        }

        override fun min(): A = when {
            left.isEmpty -> value
            else         -> left.min()
        }

        override fun pathLengths(currentDepth: Int, depths: List<Int>): List<Int> = when {
            right.isEmpty && left.isEmpty -> depths.cons(currentDepth)
            else                          -> List.concat(left.pathLengths(currentDepth + 1, depths),
                                                         right.pathLengths(currentDepth + 1, depths))
        }

        override fun pathColors(currentColorList: List<Color>, paths: List<List<Color>>): List<List<Color>> =
            when {
                right.isEmpty && left.isEmpty -> paths.cons(currentColorList.cons(color))
                else                          -> List.concat(left.pathColors(currentColorList.cons(color), paths),
                                                             right.pathColors(currentColorList.cons(color), paths))
            }

        override fun get(element: A): Result<A> = when {
            element < this.value -> left[element]
            element > this.value -> right[element]
            else                 -> Result(this.value)
        }

        override fun getT(element: A): Result<T<A>> = when {
            element < this.value -> left.getT(element)
            element > this.value -> right.getT(element)
            else                 -> Result(this)
        }

        private fun remove(): Tree<A> = when {
            isTR && left.isEmpty && right.isEmpty -> E
            isTB && left.isEmpty && right.isEmpty -> EE
            isTB && left.isEmpty && right.isTR    -> T(B, right.left, right.value, right.right)
            isTB && left.isTR && right.isEmpty    -> T(B, left.left, left.value, left.right)
            left.isEmpty                          -> bubble(right.color, right.left, right.value, right.right)
            else                                  -> bubble(color, left.removeMax(), left.max(), right)
        }

        override fun removeMax(): Tree<A> = when {
            right.isEmpty -> remove()
            else          -> bubble(color, left, value, right.removeMax())
        }

        // Post order right:
        override fun <B> foldLeft(identity: B, f: (B) -> (A) -> B, g: (B) -> (B) -> B): B =
            g(right.foldLeft(identity, f, g))(f(left.foldLeft(identity, f, g))(this.value))

        // Pre order left
        override fun <B> foldRight(identity: B, f: (A) -> (B) -> B, g: (B) -> (B) -> B): B =
            g(f(this.value)(left.foldRight(identity, f, g)))(right.foldRight(identity, f, g))

        override fun <B> foldInOrder(identity: B, f: (B) -> (A) -> (B) -> B): B =
            f(left.foldInOrder(identity, f))(value)(right.foldInOrder(identity, f))

        override fun <B> foldInReverseOrder(identity: B, f: (B) -> (A) -> (B) -> B): B =
            f(right.foldInReverseOrder(identity, f))(value)(left.foldInReverseOrder(identity, f))

        override fun <B> foldPreOrder(identity: B, f: (A) -> (B) -> (B) -> B): B =
            f(value)(left.foldPreOrder(identity, f))(right.foldPreOrder(identity, f))

        override fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (A) -> B): B =
            f(left.foldPostOrder(identity, f))(right.foldPostOrder(identity, f))(value)

        override fun redder(): Tree<A> = T(color.redder, left, value, right)

        override fun add(element: A): Tree<A> = when {
            element < this.value -> balance(this.color, this.left.add(element), this.value, this.right)
            element > this.value -> balance(this.color, this.left, this.value, this.right.add(element))
            else                 -> T(this.color, this.left, element, this.right)
        }

        override fun delete(element: A): Tree<A> = when {
            element < this.value -> bubble(this.color, this.left.delete(element), this.value, this.right)
            element > this.value -> bubble(this.color, this.left, this.value, this.right.delete(element))
            else                 -> remove()
        }

        override fun toString(): String = String.format("(T %s %s %s %s)", color, left, value, right)
    }

    sealed class Color {

        internal abstract val blacker: Color
        internal abstract val redder: Color

        // Red
        internal object R: Color() {

            override val blacker: Color = B

            override val redder: Color = NB

            override fun toString(): String = "R"
        }

        // Black
        internal object B: Color() {

            override val blacker: Color = BB

            override val redder: Color = R

            override fun toString(): String = "B"
        }

        // DoubleBlack
        internal object BB: Color() {

            override val blacker: Color by lazy { throw IllegalStateException("Can't make DoubleBlack blacker") }

            override val redder: Color = B

            override fun toString(): String = "BB"
        }

        // NegativeBlack
        internal object NB: Color() {

            override val blacker: Color = R

            override val redder: Color by lazy { throw IllegalStateException("Can't make NegativeBlack redder") }

            override fun toString(): String = "NB"
        }
    }

    companion object {

        operator fun <A: Comparable<A>> invoke(): Tree<A> = E

        fun <A: Comparable<A>> toString(ree: Tree<A>): String {
            val tableHeight = ree.height + 1
            val tableWidth = Math.pow(2.0, (ree.height + 1).toDouble()).toInt() - 1
            val table: Array<Array<String>> = Array(tableHeight) { Array(tableWidth) { "    " } }
            val hPosition = tableWidth / 2
            val vPosition = ree.height
            val result = makeTable(table, ree, hPosition, vPosition)
            val sb = StringBuilder()
            for (l in result.size downTo 1) {
                for (c in 0 until result[0].size) {
                    sb.append(makeCell(result[l - 1][c]))
                }
                sb.append("\n")
            }
            return sb.toString()
        }

        private fun makeCell(string: String): String = when (string.length) {
                1    -> " $string  "
                2    -> " $string "
                3    -> string + " "
                else -> string
            }

        private fun <A: Comparable<A>> makeTable(table: Array<Array<String>>,
                                                 tree: Tree<A>,
                                                 hPosition: Int,
                                                 vPosition: Int): Array<Array<String>> =
            when (tree) {
                is Empty -> table
                is T     -> {
                    val shift = Math.pow(2.0, (tree.height - 1).toDouble()).toInt()
                    val lhPosition = hPosition - shift
                    val rhPosition = hPosition + shift
                    table[vPosition][hPosition] = "${tree.color}${tree.value}"
                    val t2 = makeTable(table, tree.left, lhPosition, vPosition - 1)
                    makeTable(t2, tree.right, rhPosition, vPosition - 1)
                }
            }
    }
}

