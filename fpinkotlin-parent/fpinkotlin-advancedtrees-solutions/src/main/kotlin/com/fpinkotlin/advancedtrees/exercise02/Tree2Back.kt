package com.fpinkotlin.advancedtrees.exercise02

import com.fpinkotlin.common.List
import com.fpinkotlin.common.List.Companion.concat
import com.fpinkotlin.common.List.Companion.cons
import com.fpinkotlin.common.Result


sealed class Tree2Back<A: Comparable<A>> {

    internal abstract val isE: Boolean

    internal abstract val isT: Boolean
    internal abstract val isB: Boolean
    internal abstract val isBB: Boolean
    internal abstract val isTB: Boolean
    internal abstract val isTR: Boolean

    internal abstract val isTNB: Boolean
    abstract val isEmpty: Boolean

    private val g: (List<A>) -> (List<A>) -> List<A> = { a -> { b -> concat(a, b) } }
    private val f: (List<A>) -> (A) -> List<A> = { l -> { a ->  cons(a, l) } }

    abstract fun contains(element: A): Boolean
    abstract fun max(): A
    abstract fun min(): A
    protected abstract fun removeMax(): Tree2Back<A>
    abstract fun pathLengths(currentDepth: Int, depths: List<Int>): List<Int>
    protected abstract fun pathColors(currentColorList: List<Color>, paths: List<List<Color>>): List<List<Color>>

    fun pathLengths(): List<Int> = pathLengths(0, List())
    protected fun pathColors(): List<List<Color>> = pathColors(List(), List())

    abstract operator fun get(element: A): Result<A>
    protected abstract fun getT(element: A): Result<T<A>>

    abstract fun size(): Int
    abstract fun height(): Int

    abstract fun <B> foldLeft(identity: B, f: (B) -> (A) -> B, g: (B) -> (B) -> B): B
    abstract fun <B> foldRight(identity: B, f: (A) -> (B) -> B, g: (B) -> (B) -> B): B
    abstract fun <B> foldInOrder(identity: B, f: (B) -> (A) -> (B) -> B): B
    abstract fun <B> foldInReverseOrder(identity: B, f: (B) -> (A) -> (B) -> B): B
    abstract fun <B> foldPreOrder(identity: B, f: (A) -> (B) -> (B) -> B): B
    abstract fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (A) -> B): B

    internal abstract fun right(): Tree2Back<A>
    internal abstract fun left(): Tree2Back<A>
    internal abstract fun value(): A
    internal abstract fun color(): Color
    internal abstract fun redder(): Tree2Back<A>

    internal abstract fun ins(value: A): Tree2Back<A>

    fun insert(value: A): Tree2Back<A> = blacken(ins(value))

    fun delete(value: A): Tree2Back<A> = blacken(del(value))

    internal abstract fun del(element: A): Tree2Back<A>

    protected fun blacken(tree: Tree2Back<A>): Tree2Back<A> = when {
        tree.isEmpty -> E()
        else      -> T(B, tree.left(), tree.value(), tree.right())
    }

    protected fun redden(tree: Tree2Back<A>): Tree2Back<A> = when {
        tree.isEmpty -> throw IllegalStateException("Empty trees may not be reddened")
        else      -> T(R, tree.left(), tree.value(), tree.right())
    }

    fun toList(): List<A> {
        return foldLeft(List(), f, g)
    }

    private abstract class Empty<A: Comparable<A>>: Tree2Back<A>() {

        override val isT: Boolean = false

        override val isTB: Boolean = false

        override val isTR: Boolean = false

        override val isEmpty: Boolean = true

        override val isTNB: Boolean = false

        override fun removeMax(): Tree2Back<A> = throw IllegalStateException("removeMax called on Empty")

        override fun max(): A = throw IllegalStateException("max called on Empty")

        override fun min(): A = throw IllegalStateException("min called on Empty")

        override fun contains(element: A): Boolean = false

        override fun pathLengths(currentDepth: Int, depths: List<Int>): List<Int> = List()

        override fun get(element: A): Result<A> = Result()

        override fun getT(element: A): Result<T<A>> = Result()

        override fun size(): Int = 0

        override fun height(): Int = -1

        // Post order right:
        override fun <B> foldLeft(identity: B, f: (B) -> (A) -> B, g: (B) -> (B) -> B): B = identity

        // Pre order left
        override fun <B> foldRight(identity: B, f: (A) -> (B) -> B, g: (B) -> (B) -> B): B = identity

        override fun <B> foldInOrder(identity: B, f: (B) -> (A) -> (B) -> B): B = identity

        override fun <B> foldInReverseOrder(identity: B, f: (B) -> (A) -> (B) -> B): B = identity

        override fun <B> foldPreOrder(identity: B, f: (A) -> (B) -> (B) -> B): B = identity

        override fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (A) -> B): B = identity

        override fun right(): Tree2Back<A> = e()

        override fun left(): Tree2Back<A> = e()

        override fun value(): A = throw IllegalStateException("value called on Empty")

        override fun ins(value: A): Tree2Back<A> = T(R, empty(), value, empty())

        override fun del(element: A): Tree2Back<A> = e()

        override fun pathColors(currentColorList: List<Color>, paths: List<List<Color>>): List<List<Color>> =
            paths.cons(currentColorList)
    }

    private class E<A: Comparable<A>>: Empty<A>() {

        override val isE: Boolean  = true

        override val isB: Boolean = true

        override val isBB: Boolean = false

        override fun color(): Color = R

        override fun redder(): Tree2Back<A> = this

        override fun toString(): String = "E"
    }

    private class EE<A: Comparable<A>>: Empty<A>() {

        override val isE: Boolean = true

        override val isB: Boolean = false

        override val isBB: Boolean = true

        override fun color(): Color = BB

        override fun redder(): Tree2Back<A> = e()

        override fun toString(): String = "EE"
    }

    protected class T<A: Comparable<A>> internal constructor(private val color: Color,
                                                             private val left: Tree2Back<A>,
                                                             private val value: A,
                                                             private val right: Tree2Back<A>): Tree2Back<A>() {

        private val length: Int = left.size() + 1 + right.size()

        private val depth: Int = Math.max(left.height(), right.height()) + 1

        override val isB: Boolean = this.color.isB

        override val isBB: Boolean = this.color.isBB

        override val isTB: Boolean = this.color.isB || this.color.isBB

        override val isTR: Boolean = this.color.isR

        override val isTNB: Boolean = this.color.isNB

        override val isE: Boolean = false

        override val isT: Boolean = true

        override val isEmpty: Boolean = false

        private fun balance(color: Color, left: Tree2Back<A>, value: A, right: Tree2Back<A>): Tree2Back<A> = when {

            // balance B (T R (T R a x b) y c) z d = T R (T B a x b) y (T B c z d)
            color.isB && left.isTR && left.left().isTR ->
                T(R, blacken(left.left()), left.value(), T(B, left.right(), value,right))

            // balance B (T R a x (T R b y c)) z d = T R (T B a x b) y (T B c z d)
            color.isB && left.isTR && left.right().isTR ->
                T(R, T(B, left.left(), left.value(), left.right().left()), left.right().value(),
                  T(B, left.right().right(), value, right))

            // balance B a x (T R (T R b y c) z d) = T R (T B a x b) y (T B c z d)
            color.isB && right.isTR && right.left().isTR ->
                T(R, T(B, left, value, right.left().left()), right.left().value(),
                  T(B, right.left().right(), right.value(), right.right()))

            // balance B a x (T R b y (T R c z d)) = T R (T B a x b) y (T B c z d)
            color.isB && right.isTR && right.right().isTR ->
                T(R, T(B, left, value, right.left()), right.value(), blacken(right.right()))

            // balance BB (T R (T R a x b) y c) z d = T B (T B a x b) y (T B c z d)
            color.isBB && left.isTR && left.left().isTR ->
                T(B, blacken(left.left()), left.value(), T(B, left.right(), value, right))

            // balance BB (T R a x (T R b y c)) z d = T B (T B a x b) y (T B c z d)
            color.isBB && left.isTR && left.right().isTR ->
                T(B, T(B, left.left(), left.value(), left.right().left()), left.right().value(),
                  T(B, left.right().right(), value, right))

            // balance BB a x (T R (T R b y c) z d) = T B (T B a x b) y (T B c z d)
            color.isBB && right.isTR && right.left().isTR ->
                T(B, T(B, left, value, right.left().left()), right.left().value(),
                  T(B, right.left().right(), right.value(), right.right()))

            // balance BB a x (T R b y (T R c z d)) = T B (T B a x b) y (T B c z d)
            color.isBB && right.isTR && right.right().isTR ->
                T(B, T(B, left, value, right.left()), right.value(), blacken(right.right()))

            // balance BB a x (T NB (T B b y c) z d@(T B _ _ _)) = T B (T B a x b) y (balance B c z (redden d))
            color.isBB && right.isTNB && right.left().isTB && right.right().isTB ->
                T(B, T(B, left, value, right.left().left()), right.left().value(),
                  balance(B, right.left().right(), right.value(), redden(right.right())))

            // balance BB (T NB a@(T B _ _ _) x (T B b y c)) z d = T B (balance B (redden a) x b) y (T B c z d)
            color.isBB && left.isTNB && left.left().isTB && left.right().isTB ->
                T(B, balance(B, redden(left.left()), left.value(), left.right().left()), left.right().value(),
                  T(B, left.right().right(), value, right))

            // balance color a x b = T color a x b
            else -> T(color, left, value, right)
        }

        private fun bubble(color: Color, left: Tree2Back<A>, value: A, right: Tree2Back<A>): Tree2Back<A> = when {
            left.isBB || right.isBB -> balance(color.blacker(), left.redder(), value, right.redder())
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

        override fun getT(element: A): Result<T<A>> {
            return when {
                element < this.value -> left.getT(element)
                element > this.value -> right.getT(element)
                else                 -> Result(this)
            }
        }

        private fun remove(): Tree2Back<A> {
            if (isTR && left.isEmpty && right.isEmpty) {
                return e()
            }
            if (isTB && left.isEmpty && right.isEmpty) {
                return ee()
            }
            if (isTB && left.isEmpty && right.isTR) {
                return T(B, right.left(), right.value(), right.right())
            }
            if (isTB && left.isTR && right.isEmpty) {
                return T(B, left.left(), left.value(), left.right())
            }
            return if (left.isEmpty) {
                bubble(right.color(), right.left(), right.value(), right.right())
            } else bubble(color, left.removeMax(), left.max(), right)
        }

        override fun removeMax(): Tree2Back<A> {
            return if (right.isEmpty) {
                remove()
            } else bubble(color, left, value, right.removeMax())
        }

        override fun size(): Int {
            return length
        }

        override fun height(): Int {
            return depth
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

        override fun right(): Tree2Back<A> = right

        override fun left(): Tree2Back<A> = left

        override fun value(): A = value

        override fun color(): Color = color

        override fun redder(): Tree2Back<A> = T(color.redder(), left, value, right)

        override fun ins(value: A): Tree2Back<A> = when {
            value < this.value -> balance(this.color, this.left.ins(value), this.value, this.right)
            value > this.value -> balance(this.color, this.left, this.value, this.right.ins(value))
            else               -> T(this.color, this.left, value, this.right)
        }

        override fun del(element: A): Tree2Back<A> = when {
            element < this.value -> bubble(this.color, this.left.del(element), this.value, this.right)
            element > this.value -> bubble(this.color, this.left, this.value, this.right.del(element))
            else                 -> remove()
        }

        override fun toString(): String = String.format("(T %s %s %s %s)", color, left, value, right)
    }

    abstract class Color {
        internal abstract val isR: Boolean
        internal abstract val isB: Boolean
        internal abstract val isBB: Boolean
        internal abstract val isNB: Boolean
        internal abstract fun blacker(): Color
        internal abstract fun redder(): Color
    }

    // Red
    private object R: Color() {

        override val isR: Boolean = true

        override val isB: Boolean = false

        override val isBB: Boolean = false

        override val isNB: Boolean = false

        override fun blacker(): Color = B

        override fun redder(): Color = NB

        override fun toString(): String = "R"
    }

    // Black
    private object B: Color() {

        override val isR: Boolean = false

        override val isB: Boolean = true

        override val isBB: Boolean = false

        override val isNB: Boolean = false

        override fun blacker(): Color = BB

        override fun redder(): Color = R

        override fun toString(): String = "B"
    }

    // DoubleBlack
    private object BB: Color() {

        override val isR: Boolean = false

        override val isB: Boolean = false

        override val isBB: Boolean = true

        override val isNB: Boolean = false

        override fun blacker(): Color = throw IllegalStateException("Can't make DoubleBlack blacker")

        override fun redder(): Color = B

        override fun toString(): String = "BB"
    }

    // NegativeBlack
    private object NB: Color() {

        override val isR: Boolean = false

        override val isB: Boolean = false

        override val isBB: Boolean = false

        override val isNB: Boolean = true

        override fun blacker(): Color = R

        override fun redder(): Color = throw IllegalStateException("Can't make NegativeBlack redder")

        override fun toString(): String = "NB"
    }

    companion object {

        fun <A: Comparable<A>> empty(): Tree2Back<A> = E()

        private fun <A: Comparable<A>> e(): Tree2Back<A> = E()

        private fun <A: Comparable<A>> ee(): Tree2Back<A> = EE()

        fun log2nlz(n: Int): Int = when (n) {
            0    -> 0
            else -> 31 - Integer.numberOfLeadingZeros(n)
        }

        fun <A: Comparable<A>> toString(ree: Tree2Back<A>): String {
            val tableHeight = ree.height() + 1
            val tableWidth = Math.pow(2.0, (ree.height() + 1).toDouble()).toInt() - 1
            val table: Array<Array<String>> = Array(tableHeight) { Array(tableWidth) { "" } }
            val hPosition = tableWidth / 2
            val vPosition = ree.height()
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

        private fun makeCell(string: String?): String = when (string) {
            null -> "    "
            else -> when (string.length) {
                0    -> "    "
                1    -> " $string  "
                2    -> " $string "
                3    -> string + " "
                else -> string
            }
        }

        private fun <A: Comparable<A>> makeTable(table: Array<Array<String>>,
                                                 tree: Tree2Back<A>,
                                                 hPosition: Int,
                                                 vPosition: Int): Array<Array<String>> =
            when (tree) {
                is Empty -> table
                is T     -> {
                    val shift = Math.pow(2.0, (tree.height() - 1).toDouble()).toInt()
                    val lhPosition = hPosition - shift
                    val rhPosition = hPosition + shift
                    table[vPosition][hPosition] = "${tree.color()}${tree.value()}"
                    val t2 = makeTable(table, tree.left(), lhPosition, vPosition - 1)
                    makeTable(t2, tree.right(), rhPosition, vPosition - 1)
                }
            }
    }
}
