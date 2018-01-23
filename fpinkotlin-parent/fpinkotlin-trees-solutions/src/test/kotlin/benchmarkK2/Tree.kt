package benchmarkK2

import benchmarkK2.Tree.Companion.empty
import benchmarkK2.Tree.Companion.log2nlz
import com.fpinkotlin.common.List
import com.fpinkotlin.common.range
import com.fpinkotlin.trees.common.Result
import com.fpinkotlin.trees.common.getOrElse
import com.fpinkotlin.trees.common.orElse


sealed class Tree<out A: Comparable<@UnsafeVariance A>> {
    abstract val isEmpty: Boolean

    abstract fun value(): A
    internal abstract fun left(): Tree<A>
    internal abstract fun right(): Tree<A>
    internal abstract operator fun rem(a: @UnsafeVariance A): Tree<A>
    internal abstract fun mrg(a: Tree<@UnsafeVariance A>): Tree<A>

    abstract fun member(a: @UnsafeVariance A): Boolean
    abstract fun size(): Int
    abstract fun height(): Int
    abstract fun max(): Result<A>
    abstract fun min(): Result<A>
    abstract fun remove(a: @UnsafeVariance A): Tree<A>

    internal fun ins(a: @UnsafeVariance A): Tree<A> = when (this) {
        Empty -> insert(a)
        is T -> if (a.compareTo(this.value) < 0)
                    T(left.ins(a), this.value, right)
                else if (a.compareTo(this.value) > 0)
                    T(left, this.value, right.ins(a))
                else
                    T(this.left, a, this.right)
    }

    fun insert(a: @UnsafeVariance A): Tree<A> = when (this) {
        Empty -> T(Empty, a, Empty)
        is T -> {
            val t = ins(a)
            if (t.height() > log2nlz(t.size()) * 100) balance(t) else t
        }
    }

    abstract fun merge(a: Tree<@UnsafeVariance A>): Tree<A>
//    abstract fun merge(a: @UnsafeVariance A, right: Tree<@UnsafeVariance A>): Tree<A>
    abstract fun toListInOrderRight(): List<A>

    fun merge(a: @UnsafeVariance A, right: Tree<@UnsafeVariance A>): Tree<A> = when (this) {
        is Empty ->  right.min().map { min ->
                if (a.compareTo(min) < 0)
                    T(Empty, a, right)
                else
                    right.insert(a)
            }.getOrElse(this.insert(a))
        is T -> if (right.isEmpty)
                    max().map({ max ->
                                  if (max.compareTo(a) > 0)
                                      insert(a)
                                  else
                                      T(this, a, right)
                              }).getOrElse(this)
                else
                    max().flatMap({ lmax ->
                                      right.min().map({ rmin ->
                                                          if (a.compareTo(lmax) > 0 && a.compareTo(rmin) < 0) T(this, a,
                                                                                                                right) else merge(
                                                              right).insert(a)
                                                      })
                                  }).getOrElse(right)
        else -> Empty
    }


    /**
     * Merges two subtrees with the particularity that all elements of one
     * are either greater or smaller than all elements of the other.
     *
     * This is an optimized merge for removal of the value, when we need to merge
     * the remaining right and left tree.
     */
    protected abstract fun removeMerge(ta: Tree<@UnsafeVariance A>): Tree<A>

    abstract fun <B> foldLeft(identity: B, f: (B) -> (A) -> B, g: (B) -> (B) -> B): B

    abstract fun <B> foldRight(identity: B, f: (A) -> (B) -> B, g: (B) -> (B) -> B): B

    abstract fun <B> foldInOrder(identity: B, f: (B) -> (A) -> (B) -> B): B

    abstract fun <B> foldPreOrder(identity: B, f: (A) -> (B) -> (B) -> B): B

    abstract fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (A) -> B): B

//    abstract fun <B: Comparable<B>> map(f: (A) -> B): Tree<B>

    protected abstract fun rotateLeft(): Tree<A>

    protected abstract fun rotateRight(): Tree<A>

    private object Empty: Tree<Nothing>() {

        override val isEmpty: Boolean
            get() = true

        override fun value(): Nothing {
            throw IllegalStateException("value() called on empty")
        }

        override fun left(): Tree<Nothing> {
            throw IllegalStateException("left() called on empty")
        }

        override fun right(): Tree<Nothing> {
            throw IllegalStateException("right() called on empty")
        }

        override fun member(a: Nothing): Boolean {
            return false
        }

        override fun size(): Int {
            return 0
        }

        override fun height(): Int {
            return -1
        }

        override fun max(): Result<Nothing> {
            return Result()
        }

        override fun min(): Result<Nothing> {
            return Result()
        }

        override fun remove(a: Nothing): Tree<Nothing> {
            return this
        }

         override fun rem(a: Nothing): Tree<Nothing> {
            return this
        }

        override fun merge(a: Tree<Nothing>): Tree<Nothing> {
            return a
        }

         override fun mrg(a: Tree<Nothing>): Tree<Nothing> {
            return a
        }

        override fun toListInOrderRight(): List<Nothing> {
            return List()
        }

        override fun removeMerge(ta: Tree<Nothing>): Tree<Nothing> {
            return ta
        }

        override fun <B> foldLeft(identity: B, f: (B) -> (Nothing) -> B, g: (B) -> (B) -> B): B {
            return identity
        }

        override fun <B> foldRight(identity: B, f: (Nothing) -> (B) -> B, g: (B) -> (B) -> B): B {
            return identity
        }

        override fun <B> foldInOrder(identity: B, f: (B) -> (Nothing) -> (B) -> B): B {
            return identity
        }

        override fun <B> foldPreOrder(identity: B, f: (Nothing) -> (B) -> (B) -> B): B {
            return identity
        }

        override fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (Nothing) -> B): B {
            return identity
        }

//        override fun <B: Comparable<B>> map(f: (A) -> B): Tree<B> {
//            return empty()
//        }

        override fun rotateLeft(): Tree<Nothing> {
            return this
        }

        override fun rotateRight(): Tree<Nothing> {
            return this
        }

        override fun toString(): String {
            return "E"
        }
    }

    private class T<A: Comparable<A>> constructor(internal val left: Tree<A>,
                                                  internal val value: A,
                                                  internal val right: Tree<A>): Tree<A>() {

        private val height: Int
        private val size: Int

        override val isEmpty: Boolean
            get() = false

        init {
            this.height = 1 + Math.max(left.height(), right.height())
            this.size = 1 + left.size() + right.size()
        }

        override fun value(): A {
            return value
        }

        override fun left(): Tree<A> {
            return left
        }

        override fun right(): Tree<A> {
            return right
        }

        override fun member(value: A): Boolean {
            return if (value.compareTo(this.value) < 0)
                left.member(value)
            else
                value.compareTo(this.value) <= 0 || right.member(value)
        }

        override fun size(): Int {
            return size
        }

        override fun height(): Int {
            return height
        }

        override fun max(): Result<A> {
            return right.max().orElse { Result(value) }
        }

        override fun min(): Result<A> {
            return left.min().orElse { Result(value) }
        }

        override fun remove(a: A): Tree<A> {
            val t = rem(a)
            return if (t.height() > log2nlz(t.size()) * 100) balance(t) else t
        }

        override fun rem(a: A): Tree<A> {
            return if (a.compareTo(this.value) < 0) {
                T(left.rem(a), value, right)
            } else if (a.compareTo(this.value) > 0) {
                T(left, value, right.rem(a))
            } else {
                left.removeMerge(right)
            }
        }

        override fun merge(a: Tree<A>): Tree<A> {
            val t = mrg(a)
            return if (t.height() > log2nlz(t.size()) * 100) balance(t) else t
        }

        override fun mrg(a: Tree<A>): Tree<A> {
            if (a.isEmpty) {
                return this
            }
            if (a.value().compareTo(this.value) > 0) {
                return T(left, value, right.mrg(T(empty(), a.value(), a.right()))).mrg(a.left())
            }
            return if (a.value().compareTo(this.value) < 0) {
                T(left.mrg(T(a.left(), a.value(), empty())), value, right).mrg(a.right())
            } else T(left.mrg(a.left()), value, right.mrg(a.right()))
        }

//        override fun toListInOrderRight(): List<A> {
//            return unBalanceRight(List(), this).eval()
//        }
//
//        private fun unBalanceRight(acc: List<A>, tree: Tree<A>): TailCall<List<A>> {
//            return if (tree.isEmpty)
//                TailCall.ret(acc)
//            else if (tree.left().isEmpty)
//                TailCall.sus({ unBalanceRight(acc.cons(tree.value()), tree.right()) })
//            else
//                TailCall.sus({ unBalanceRight(acc, tree.rotateRight()) })
//        }

        override fun toListInOrderRight(): List<A> {
            return unBalanceRight(List(), this)
        }

        private tailrec fun unBalanceRight(acc: List<A>, tree: Tree<A>): List<A> {
            return if (tree.isEmpty)
                acc
            else if (tree.left().isEmpty)
                unBalanceRight(acc.cons(tree.value()), tree.right())
            else
                unBalanceRight(acc, tree.rotateRight())
        }

        override fun removeMerge(ta: Tree<A>): Tree<A> {
            if (ta.isEmpty) {
                return this
            }
            if (ta.value().compareTo(value) < 0) {
                return T(left.removeMerge(ta), value, right)
            } else if (ta.value().compareTo(value) > 0) {
                return T(left, value, right.removeMerge(ta))
            }
            throw IllegalStateException("Shouldn't be merging two subtrees with the same value")
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

        override fun <B> foldPreOrder(identity: B, f: (A) -> (B) -> (B) -> B): B {
            return f(value)(left.foldPreOrder(identity, f))(right.foldPreOrder(identity, f))
        }

        override fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (A) -> B): B {
            return f(left.foldPostOrder(identity, f))(right.foldPostOrder(identity, f))(value)
        }

//        override fun <B: Comparable<B>> map(f: (A) -> B): Tree<B> {
//            return foldInOrder(Tree.empty(), { t1 -> { i -> { t2 -> Tree.tree<A>(t1, f(i), t2) } } })
//        }

        override fun rotateLeft(): Tree<A> {
            return if (right.isEmpty)
                this
            else
                T(T(left, value, right.left()), right.value(), right.right())
        }

        override fun rotateRight(): Tree<A> {
            return if (left.isEmpty)
                this
            else
                T(left.left(), left.value(), T(left.right(), value, right))
        }

        override fun toString(): String {
            return String.format("(T %s %s %s)", left, value, right)
        }
    }

    companion object {


        fun <A: Comparable<A>> empty(): Tree<A> {
            return Empty
        }

        fun <A: Comparable<A>> toTree(list: List<A>): Tree<A> {
            return list.foldLeft(empty()) { t: Tree<A> -> { a: A -> t.insert(a) } }
        }

        fun <A: Comparable<A>> tree(vararg array: A): Tree<A> {
            return toTree(List(*array))
        }

        fun <A: Comparable<A>> lt(first: A, second: A): Boolean {
            return first.compareTo(second) < 0
        }

        fun <A: Comparable<A>> lt(first: A, second: A, third: A): Boolean {
            return lt(first, second) && lt(second, third)
        }

        fun <A: Comparable<A>> tree(t1: Tree<A>, a: A, t2: Tree<A>): Tree<A> {
            return if (ordered(t1, a, t2))
                T(t1, a, t2)
            else if (ordered(t2, a, t1))
                T(t2, a, t1)
            else
                Tree.empty<A>().insert(a).merge(t1).merge(t2)
        }

        fun <A: Comparable<A>> ordered(left: Tree<A>, a: A, right: Tree<A>): Boolean {
            return (left.max().flatMap({ lMax -> right.min().map({ rMin -> lt(lMax, a, rMin) }) }).getOrElse(
                left.isEmpty && right.isEmpty)
                || left.min().mapEmpty().flatMap({ ignore -> right.min().map({ rMin -> lt(a, rMin) }) }).getOrElse(false)
                || right.min().mapEmpty().flatMap({ ignore -> left.max().map({ lMax -> lt(lMax, a) }) }).getOrElse(false))
        }

        fun <A : Comparable<A>> isUnBalanced(tree: Tree<A>): Boolean = when (tree) {
            Empty -> false
            is T -> Math.abs(tree.left.height() - tree.right.height()) > (tree.size() - 1) % 2
        }

        fun <A : Comparable<A>> balance(tree: Tree<A>): Tree<A> =
                balanceHelper(tree.toListInOrderRight()
                        .foldLeft(Empty) { t: Tree<A> -> { a: A -> T(Empty, a, t) } })

        private fun <A : Comparable<A>> balanceHelper(tree: Tree<A>): Tree<A> = when (tree) {
            Empty -> tree
            is T -> when {
                tree.height() > log2nlz(tree.size()) &&
                        Math.abs(tree.left.height() - tree.right.height()) > 1 ->
                    balanceHelper(balanceFirstLevel(tree))
                else -> T(balanceHelper(tree.left), tree.value, balanceHelper(tree.right))
            }
        }

        private fun <A : Comparable<A>> balanceFirstLevel(tree: T<A>): Tree<A> =
                unfold(tree) { t: Tree<A> ->
                    when {
                        isUnBalanced(t) -> when {
                            tree.right.height() > tree.left.height() -> Result(t.rotateLeft() as T<A>)
                            else -> Result(t.rotateRight() as T<A>)
                        }
                        else -> Result()
                    }
                }

//        fun <A: Comparable<A>> balance(tree: Tree<A>): Tree<A> {
//            return balance_(tree.toListInOrderRight().foldLeft(Tree.empty<A>()) { t -> { a -> T(empty<A>(), a, t) } })
//        }
//
//        fun <A: Comparable<A>> balance_(tree: Tree<A>): Tree<A> {
//            return if (!tree.isEmpty && tree.height() > log2nlz(tree.size()))
//                if (Math.abs(tree.left().height() - tree.right().height()) > 1)
//                    balance_(balanceFirstLevel(tree))
//                else
//                    T(balance_(tree.left()), tree.value(), balance_(tree.right()))
//            else
//                tree
//        }
//
//        private fun <A: Comparable<A>> balanceFirstLevel(tree: Tree<A>): Tree<A> {
//            return unfold(tree, { t: Tree<A> ->
//                if (isUnBalanced<A>(t))
//                    if (tree.right().height() > tree.left().height())
//                        Result(t.rotateLeft())
//                    else
//                        Result(t.rotateRight())
//                else
//                    Result()
//            })
//        }
//
//        private fun <A: Comparable<A>> isUnBalanced(tree: Tree<A>): Boolean {
//            // Difference must be 0 if total size of branches is even and 1 if size is odd
//            return Math.abs(tree.left().height() - tree.right().height()) > (tree.size() - 1) % 2
//        }


        fun log2nlz(n: Int): Int {
            return if (n == 0)
                0
            else
                31 - Integer.numberOfLeadingZeros(n)
        }

        fun <A> unfold(a: A, f: (A) -> Result<A>): A {
            val ra = Result(a)
            return unfold(Pair(ra, ra), f).second.getOrElse(a)
        }

//        fun <A> unfold(a: Pair<Result<A>, Result<A>>, f: (A) -> Result<A>): TailCall<Pair<Result<A>, Result<A>>> {
//            val x = a.second.flatMap{ f() }
//            return if (x.isSuccess())
//                TailCall.sus({ unfold(Pair(a.second, x), f) })
//            else
//                TailCall.ret(a)
//        }

        tailrec fun <A> unfold(a: Pair<Result<A>, Result<A>>, f: (A) -> Result<A>): Pair<Result<A>, Result<A>> {
            val x = a.second.flatMap { f(it) }
            return if (x.exists { true })
                 unfold(Pair(a.second, x), f)
            else
                a
        }
    }
}

/** Slightly slower than Java, but comparable:
 *
    4240
    4300
    16000
    true
    true
    true
 */
fun main(args: Array<String>) {
    val timeFactor = 500
    val limit = 100_000
    val maxTime = 2 * log2nlz(limit + 1) * timeFactor
    val orderedTestList = range(0, limit)
    val time = System.currentTimeMillis()
    val temp = orderedTestList.foldLeft(empty()) { t: Tree<Int> -> { i -> t.insert(i) } }
    println(System.currentTimeMillis() - time)
    val tree =Tree.balance(temp)
    val duration = System.currentTimeMillis() - time
    println(duration)
    println(maxTime)
    println(tree.size() == limit)
    println(tree.height() <= 2 * log2nlz(tree.size() + 1))
    println(duration < maxTime)

}