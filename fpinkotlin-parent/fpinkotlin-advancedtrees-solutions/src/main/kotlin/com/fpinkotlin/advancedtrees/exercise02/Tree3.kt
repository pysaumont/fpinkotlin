package com.fpinkotlin.advancedtrees.exercise02

import com.fpinkotlin.common.List
import com.fpinkotlin.common.List.Companion.concat
import com.fpinkotlin.common.List.Companion.cons
import com.fpinkotlin.common.Result


sealed class Tree3<A: Comparable<A>> {

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

    abstract fun member(elt: A): Boolean
    abstract fun max(): A
    abstract fun min(): A
    protected abstract fun removeMax(): Tree3<A>
    abstract fun pathLengths(currentDepth: Int, depths: List<Int>): List<Int>
    protected abstract fun pathColors(currentColorList: List<Color>, paths: List<List<Color>>): List<List<Color>>

    fun pathLengths(): List<Int> {
        return pathLengths(0, List())
    }

    protected fun pathColors(): List<List<Color>> {
        return pathColors(List(), List())
    }

    abstract operator fun get(elt: A): Result<A>
    protected abstract fun getT(elt: A): Result<T<A>>

    abstract fun size(): Int
    abstract fun height(): Int

    abstract fun <B> foldLeft(identity: B, f: (B) -> (A) -> B, g: (B) -> (B) -> B): B

    abstract fun <B> foldRight(identity: B, f: (A) -> (B) -> B, g: (B) -> (B) -> B): B

    abstract fun <B> foldInOrder(identity: B, f: (B) -> (A) -> (B) -> B): B

    abstract fun <B> foldInReverseOrder(identity: B, f: (B) -> (A) -> (B) -> B): B

    abstract fun <B> foldPreOrder(identity: B, f: (A) -> (B) -> (B) -> B): B

    abstract fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (A) -> B): B

    internal abstract fun right(): Tree3<A>
    internal abstract fun left(): Tree3<A>
    internal abstract fun value(): A
    internal abstract fun color(): Color
    internal abstract fun redder(): Tree3<A>

    internal abstract fun ins(value: A): Tree3<A>

    fun insert(value: A): Tree3<A> {
        return blacken(ins(value))
    }

    fun delete(value: A): Tree3<A> {
        return blacken(del(value))
    }

    internal abstract fun del(value: A): Tree3<A>

    protected fun blacken(t: Tree3<A>): Tree3<A> {
        return if (t.isEmpty)
            E()
        else
            T(B, t.left(), t.value(), t.right())
    }

    protected fun redden(t: Tree3<A>): Tree3<A> {
        if (t.isEmpty) throw IllegalStateException("Empty trees may not be reddened")
        return T(R, t.left(), t.value(), t.right())
    }

    fun toList(): List<A> {
        return foldLeft(List(), f, g)
    }

    private abstract class Empty<A: Comparable<A>>: Tree3<A>() {

        internal override val isT: Boolean
            get() = false

        internal override val isTB: Boolean
            get() = false

        internal override val isTR: Boolean
            get() = false

        override val isEmpty: Boolean
            get() = true

        internal override val isTNB: Boolean
            get() = false

        override fun removeMax(): Tree3<A> {
            throw IllegalStateException("removeMax called on Empty")
        }

        override fun max(): A {
            throw IllegalStateException("max called on Empty")
        }

        override fun min(): A {
            throw IllegalStateException("min called on Empty")
        }

        override fun member(value: A): Boolean {
            return false
        }

        override fun pathLengths(currentDepth: Int, depths: List<Int>): List<Int> {
            return List()
        }

        override fun get(value: A): Result<A> {
            return Result()
        }

        protected override fun getT(value: A): Result<T<A>> {
            return Result()
        }

        override fun size(): Int {
            return 0
        }

        override fun height(): Int {
            return -1
        }

        override fun <B> foldLeft(identity: B, f: (B) -> (A) -> B, g: (B) -> (B) -> B): B {
            // Post order right:
            return identity
        }

        override fun <B> foldRight(identity: B, f: (A) -> (B) -> B, g: (B) -> (B) -> B): B {
            // Pre order left
            return identity
        }

        override fun <B> foldInOrder(identity: B, f: (B) -> (A) -> (B) -> B): B {
            return identity
        }

        override fun <B> foldInReverseOrder(identity: B, f: (B) -> (A) -> (B) -> B): B {
            return identity
        }

        override fun <B> foldPreOrder(identity: B, f: (A) -> (B) -> (B) -> B): B {
            return identity
        }

        override fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (A) -> B): B {
            return identity
        }


        override fun right(): Tree3<A> {
            return e()
        }

        override fun left(): Tree3<A> {
            return e()
        }

        internal override fun value(): A {
            throw IllegalStateException("value called on Empty")
        }

        internal override fun ins(value: A): Tree3<A> {
            return T(R, empty(), value, empty())
        }

        internal override fun del(a: A): Tree3<A> {
            return e()
        }

        override fun pathColors(currentColorList: List<Color>, paths: List<List<Color>>): List<List<Color>> {
            return paths.cons(currentColorList)
        }
    }

    private class E<A: Comparable<A>>: Empty<A>() {

        override val isE: Boolean
            get() = true

        internal override val isB: Boolean
            get() = true

        internal override val isBB: Boolean
            get() = false

        internal override fun color(): Color {
            return R
        }

        override fun redder(): Tree3<A> {
            return this
        }

        override fun toString(): String {
            return "E"
        }
    }

    private class EE<A: Comparable<A>>: Empty<A>() {

        override val isE: Boolean
            get() = true

        internal override val isB: Boolean
            get() = false

        internal override val isBB: Boolean
            get() = true

        internal override fun color(): Color {
            return BB
        }

        override fun redder(): Tree3<A> {
            return e()
        }

        override fun toString(): String {
            return "EE"
        }
    }

    protected class T<A: Comparable<A>> internal constructor(private val color: Color,
                                                             private val left: Tree3<A>,
                                                             private val value: A,
                                                             private val right: Tree3<A>): Tree3<A>() {

        private val length: Int
        private val depth: Int

        override val isB: Boolean
            get() = this.color.isB

        override val isBB: Boolean
            get() = this.color.isBB

        internal override val isTB: Boolean
            get() = this.color.isB || this.color.isBB

        internal override val isTR: Boolean
            get() = this.color.isR

        internal override val isTNB: Boolean
            get() = this.color.isNB

        override val isE: Boolean
            get() = false

        internal override val isT: Boolean
            get() = true

        override val isEmpty: Boolean
            get() = false

        init {
            this.length = left.size() + 1 + right.size()
            this.depth = Math.max(left.height(), right.height()) + 1
        }

        private fun balance(color: Color, left: Tree3<A>, value: A, right: Tree3<A>): Tree3<A> {
            // balance B (T R (T R a x b) y c) z d = T R (T B a x b) y (T B c z d)
            if (color.isB && left.isTR && left.left().isTR) {
                return T(R, blacken(left.left()), left.value(), T(B, left.right(), value, right))
            }
            // balance B (T R a x (T R b y c)) z d = T R (T B a x b) y (T B c z d)
            if (color.isB && left.isTR && left.right().isTR) {
                return T(R, T(B, left.left(), left.value(), left.right().left()), left.right().value(),
                         T(B, left.right().right(), value, right))
            }
            // balance B a x (T R (T R b y c) z d) = T R (T B a x b) y (T B c z d)
            if (color.isB && right.isTR && right.left().isTR) {
                return T(R, T(B, left, value, right.left().left()), right.left().value(),
                         T(B, right.left().right(), right.value(), right.right()))
            }
            // balance B a x (T R b y (T R c z d)) = T R (T B a x b) y (T B c z d)
            if (color.isB && right.isTR && right.right().isTR) {
                return T(R, T(B, left, value, right.left()), right.value(), blacken(right.right()))
            }

            // balance BB (T R (T R a x b) y c) z d = T B (T B a x b) y (T B c z d)
            if (color.isBB && left.isTR && left.left().isTR) {
                return T(B, blacken(left.left()), left.value(), T(B, left.right(), value, right))
            }
            // balance BB (T R a x (T R b y c)) z d = T B (T B a x b) y (T B c z d)
            if (color.isBB && left.isTR && left.right().isTR) {
                return T(B, T(B, left.left(), left.value(), left.right().left()), left.right().value(),
                         T(B, left.right().right(), value, right))
            }
            // balance BB a x (T R (T R b y c) z d) = T B (T B a x b) y (T B c z d)
            if (color.isBB && right.isTR && right.left().isTR) {
                return T(B, T(B, left, value, right.left().left()), right.left().value(),
                         T(B, right.left().right(), right.value(), right.right()))
            }
            // balance BB a x (T R b y (T R c z d)) = T B (T B a x b) y (T B c z d)
            if (color.isBB && right.isTR && right.right().isTR) {
                return T(B, T(B, left, value, right.left()), right.value(), blacken(right.right()))
            }
            // balance BB a x (T NB (T B b y c) z d@(T B _ _ _)) = T B (T B a x b) y (balance B c z (redden d))
            if (color.isBB && right.isTNB && right.left().isTB && right.right().isTB) {
                return T(B, T(B, left, value, right.left().left()), right.left().value(),
                         balance(B, right.left().right(), right.value(), redden(right.right())))
            }
            // balance BB (T NB a@(T B _ _ _) x (T B b y c)) z d = T B (balance B (redden a) x b) y (T B c z d)
            if (color.isBB && left.isTNB && left.left().isTB && left.right().isTB) {
                return T(B, balance(B, redden(left.left()), left.value(), left.right().left()), left.right().value(),
                         T(B, left.right().right(), value, right))
            }

            // balance color a x b = T color a x b
            return T(color, left, value, right)
        }

        private fun bubble(color: Color, left: Tree3<A>, value: A, right: Tree3<A>): Tree3<A> {
            return if (left.isBB || right.isBB)
                balance(color.blacker(), left.redder(), value, right.redder())
            else
                balance(color, left, value, right)
        }

        override fun member(value: A): Boolean {
            return if (value.compareTo(this.value) < 0)
                left.member(value)
            else
                value.compareTo(this.value) <= 0 || right.member(value)
        }

        override fun max(): A {
            return if (right.isEmpty)
                value
            else
                right.max()
        }

        override fun min(): A {
            return if (left.isEmpty)
                value
            else
                left.min()
        }

        override fun pathLengths(currentDepth: Int, depths: List<Int>): List<Int> {
            return if (right.isEmpty && left.isEmpty)
                depths.cons(currentDepth)
            else
                List.concat(left.pathLengths(currentDepth + 1, depths), right.pathLengths(currentDepth + 1, depths))
        }

        override fun pathColors(currentColorList: List<Color>, paths: List<List<Color>>): List<List<Color>> {
            return if (right.isEmpty && left.isEmpty)
                paths.cons(currentColorList.cons(color))
            else
                List.concat(left.pathColors(currentColorList.cons(color), paths),
                            right.pathColors(currentColorList.cons(color), paths))
        }

        override fun get(value: A): Result<A> {
            return if (value.compareTo(this.value) < 0)
                left[value]
            else if (value.compareTo(this.value) > 0)
                right[value]
            else
                Result(this.value)
        }

        override fun getT(value: A): Result<T<A>> {
            return if (value.compareTo(this.value) < 0)
                left.getT(value)
            else if (value.compareTo(this.value) > 0)
                right.getT(value)
            else
                Result(this)
        }

        private fun remove(): Tree3<A> {
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

        override fun removeMax(): Tree3<A> {
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

        override fun <B> foldLeft(identity: B, f: (B) -> (A) -> B, g: (B) -> (B) -> B): B {
            // Post order right:
            return g(right.foldLeft(identity, f, g))(f(left.foldLeft(identity, f, g))(this.value))
        }

        override fun <B> foldRight(identity: B, f: (A) -> (B) -> B, g: (B) -> (B) -> B): B {
            // Pre order left
            return g(f(this.value)(left.foldRight(identity, f, g)))(right.foldRight(identity, f, g))
        }

        override fun <B> foldInOrder(identity: B, f: (B) -> (A) -> (B) -> B): B {
            return f(left.foldInOrder(identity, f))(value)(right.foldInOrder(identity, f))
        }

        override fun <B> foldInReverseOrder(identity: B, f: (B) -> (A) -> (B) -> B): B {
            return f(right.foldInReverseOrder(identity, f))(value)(left.foldInReverseOrder(identity, f))
        }

        override fun <B> foldPreOrder(identity: B, f: (A) -> (B) -> (B) -> B): B {
            return f(value)(left.foldPreOrder(identity, f))(right.foldPreOrder(identity, f))
        }

        override fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (A) -> B): B {
            return f(left.foldPostOrder(identity, f))(right.foldPostOrder(identity, f))(value)
        }

        override fun right(): Tree3<A> {
            return right
        }

        override fun left(): Tree3<A> {
            return left
        }

        internal override fun value(): A {
            return value
        }

        internal override fun color(): Color {
            return color
        }

        override fun redder(): Tree3<A> {
            return T(color.redder(), left, value, right)
        }

        internal override fun ins(value: A): Tree3<A> {
            return if (value.compareTo(this.value) < 0)
                balance(this.color, this.left.ins(value), this.value, this.right)
            else if (value.compareTo(this.value) > 0)
                balance(this.color, this.left, this.value, this.right.ins(value))
            else
                T(this.color, this.left, value, this.right)
        }

        internal override fun del(value: A): Tree3<A> {
            return if (value.compareTo(this.value) < 0)
                bubble(this.color, this.left.del(value), this.value, this.right)
            else if (value.compareTo(this.value) > 0)
                bubble(this.color, this.left, this.value, this.right.del(value))
            else
                remove()
        }

        override fun toString(): String {
            return String.format("(T %s %s %s %s)", color, left, value, right)
        }
    }

    abstract class Color {
        internal abstract val isR: Boolean
        internal abstract val isB: Boolean
        internal abstract val isBB: Boolean
        internal abstract val isNB: Boolean
        internal abstract fun blacker(): Color
        internal abstract fun redder(): Color
    }

    private class Red: Color() {

        override val isR: Boolean
            get() = true

        override val isB: Boolean
            get() = false

        override val isBB: Boolean
            get() = false

        override val isNB: Boolean
            get() = false

        override fun blacker(): Color {
            return B
        }

        override fun redder(): Color {
            return NB
        }

        override fun toString(): String {
            return "R"
        }
    }

    private class Black: Color() {

        override val isR: Boolean
            get() = false

        override val isB: Boolean
            get() = true

        override val isBB: Boolean
            get() = false

        override val isNB: Boolean
            get() = false

        override fun blacker(): Color {
            return BB
        }

        override fun redder(): Color {
            return R
        }

        override fun toString(): String {
            return "B"
        }
    }

    private class DoubleBlack: Color() {

        override val isR: Boolean
            get() = false

        override val isB: Boolean
            get() = false

        override val isBB: Boolean
            get() = true

        override val isNB: Boolean
            get() = false

        override fun blacker(): Color {
            throw IllegalStateException("Can't make DoubleBlack blacker")
        }

        override fun redder(): Color {
            return B
        }

        override fun toString(): String {
            return "BB"
        }
    }

    private class NegativeBlack: Color() {

        override val isR: Boolean
            get() = false

        override val isB: Boolean
            get() = false

        override val isBB: Boolean
            get() = false

        override val isNB: Boolean
            get() = true

        override fun blacker(): Color {
            return R
        }

        override fun redder(): Color {
            throw IllegalStateException("Can't make NegativeBlack redder")
        }

        override fun toString(): String {
            return "NB"
        }
    }

    companion object {

//        private val E = E()
//        private val EE = EE()
        protected var R: Color = Red()
        protected var B: Color = Black()
        private val BB = DoubleBlack()
        private val NB = NegativeBlack()

        fun <A: Comparable<A>> empty(): Tree3<A> {
            return E()
        }

        private fun <A: Comparable<A>> e(): Tree3<A> {
            return E()
        }

        private fun <A: Comparable<A>> ee(): Tree3<A> {
            return EE()
        }

        fun log2nlz(n: Int): Int {
            return if (n == 0)
                0
            else
                31 - Integer.numberOfLeadingZeros(n)
        }

        fun <A: Comparable<A>> toString(t: Tree3<A>): String {
            val tableHeight = t.height() + 1
            val tableWidth = Math.pow(2.0, (t.height() + 1).toDouble()).toInt() - 1
            val table: Array<Array<String>> = Array(tableHeight) { Array(tableWidth) { "" } }
            val hPosition = tableWidth / 2
            val vPosition = t.height()
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
                                                 t: Tree3<A>,
                                                 hPosition: Int,
                                                 vPosition: Int): Array<Array<String>> =
            when (t) {
                is Empty -> table
                is T     -> {
                    val shift = Math.pow(2.0, (t.height() - 1).toDouble()).toInt()
                    val lhPosition = hPosition - shift
                    val rhPosition = hPosition + shift
                    table[vPosition][hPosition] = "${t.color()}${t.value()}"
                    val t2 = makeTable<A>(table, t.left(), lhPosition, vPosition - 1)
                    makeTable(t2, t.right(), rhPosition, vPosition - 1)
                }
            }
    }
}
