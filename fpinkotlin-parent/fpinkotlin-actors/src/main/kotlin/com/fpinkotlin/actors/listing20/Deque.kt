package com.asn.pmdatabase.checker.actors01.listing20

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result

/**
 * A deque implementation based upon an immutable FingerTree. Enqueuing is done
 * by adding to the right, and dequeing by removing from the left. This is then not
 * an optimal implementation, but it is nearly as fast as an ArrayList and is
 * truly immutable. As a FingerTree, it is not complete, allowing only adding
 * and removing to/from right and left. There is no split, merge nor indexed
 * access. You're welcome to add these functions.
 */
sealed class Deque<T> {

    internal abstract fun left(): T

    internal abstract fun right(): T

    /**
     * Test if this Deque is empty
     *
     * @return true if this Deque is empty, false otherwise
     */
    abstract fun isEmpty(): Boolean

    /**
     * Get the size (number of elements) of this deque
     *
     * @return true if this Deque is empty, false otherwise
     */
    abstract fun size(): Int

    internal abstract fun addLeft(value: T): Deque<T>

    internal abstract fun addRight(value: T): Deque<T>

    internal abstract fun removeLeft(): Deque<T>

    internal abstract fun removeRight(): Deque<T>

    /**
     * Add an element to this Deque.
     *
     * @param elem The element to add to the Deque
     * @return A new Deque with teh element added
     */
    fun enqueue(elem: T): Deque<T> = addRight(elem)

    /**
     * Add an element to this Deque.
     *
     * @param elem The element to add to the Deque
     * @return A new Deque with teh element added
     */
    operator fun plus(elem: T): Deque<T> = addRight(elem)

    /**
     * Remove an element from this Deque. This function returns a pair consisting in a Result.Success<T>
     * and a new Deque with the element removed if the original Deque was not empty, or a pair of
     * Result.Failure and the empty Deque otherwise.
     *
     * @return A Pair of a Result<T> and a new Deque<T>
     */
    fun dequeue(): Pair<Result<T>, Deque<T>> = takeLeft()

    /**
     * Get an element from the Deque without removing it.
     *
     * @return a Result.Success<T> if the Deque is not empty, and a Result.Empty otherwise.
     */
    fun peek(): Result<T> = if (isEmpty()) Result.Empty else Result.Success(left())

    internal open fun takeLeft(): Pair<Result<T>, Deque<T>> = Pair(Result.Success(left()), removeLeft())

    internal open fun takeRight(): Pair<Result<T>, Deque<T>> = Pair(Result.Success(right()), removeRight())

    fun takeLeft(n: Int): List<T> {
        tailrec fun takeLeft(m: Int, deque: Deque<T>, acc: List<T> = List()): List<T> = when (m) {
            0    -> acc
            else -> {
                val x = deque.takeLeft()
                takeLeft(m - 1, x.second, acc.cons(x.first.getOrElse { throw IllegalStateException() }))
            }
        }
        return takeLeft(n, this)
    }

    internal class Empty<T>: Deque<T>() {

        override fun isEmpty(): Boolean = true

        override fun size(): Int = 0

        override fun left(): T  = throw IllegalStateException("Can't get left element of an empty deque")

        override fun right(): T = throw IllegalStateException("Can't get right element of an empty deque")

        override fun addLeft(value: T): Deque<T> = Single(value)

        override fun addRight(value: T): Deque<T> = Single(value)

        override fun removeLeft(): Deque<T> =
            throw IllegalStateException("Can't remove an element from left of an empty deque")

        override fun removeRight(): Deque<T> =
            throw IllegalStateException("Can't remove an element from right of an empty deque")

        override fun takeLeft(): Pair<Result<T>, Deque<T>> = Pair(Result.Empty, this)

        override fun takeRight(): Pair<Result<T>, Deque<T>> = Pair(Result.Empty, this)
    }

    private class SubTree<T>(val left: Affix<T>,
                             val center: Deque<Affix<T>>,
                             val right: Affix<T>,
                             val size: Int): Deque<T>() {

        override fun isEmpty(): Boolean = false

        override fun size(): Int = size

        override fun addLeft(value: T): Deque<T> = when {
            left.isFull() -> SubTree(Two(value, left.left()), center.addLeft(left.removeLeft()), right, size + 1)
            else          -> SubTree(left.addLeft(value), center, right, size + 1)
        }

        override fun addRight(value: T): Deque<T> = when {
            right.isFull() -> SubTree(left, center.addRight(right.removeRight()), Two(right.right(), value), size + 1)
            else           -> SubTree(left, center, right.addRight(value), size + 1)
        }

        override fun removeLeft(): Deque<T> = when {
            left.size() > 1   -> SubTree(left.removeLeft(), center, right, size - 1)
            !center.isEmpty() -> SubTree(center.left(), center.removeLeft(), right, size - 1)
            right.size() > 1  -> SubTree(One(right.left()), center, right.removeLeft(), size - 1)
            else              -> Single(right.left())
        }

        override fun removeRight(): Deque<T> = when {
            right.size() > 1  -> SubTree(left, center, right.removeRight(), size -1)
            !center.isEmpty() -> SubTree(left, center.removeRight(), center.right(), size -1)
            left.size() > 1   -> SubTree(left.removeRight(), center, One(left.right()), size -1)
            else              -> Single(left.right())
        }

        override fun left(): T = left.left()

        override fun right(): T = right.right()
    }

    private class Single<T>(val _1: T): Deque<T>() {

        override fun isEmpty(): Boolean = false

        override fun size(): Int = 1

        override fun left(): T = _1

        override fun right(): T = _1

        override fun addLeft(value: T): Deque<T> = SubTree(One(value), invoke(), One(_1), 2)

        override fun addRight(value: T): Deque<T> = SubTree(One(_1), invoke(), One(value), 2)

        override fun removeLeft(): Deque<T> = invoke()

        override fun removeRight(): Deque<T> = invoke()
    }

    internal abstract class Affix<T> {

        open fun isFull(): Boolean = false

        abstract fun left(): T

        abstract fun right(): T

        abstract fun size(): Int

        abstract fun addLeft(t: T): Affix<T>

        abstract fun addRight(t: T): Affix<T>

        abstract fun removeLeft(): Affix<T>

        abstract fun removeRight(): Affix<T>
    }

    private class One<T>(val _1: T): Affix<T>() {

        override fun size(): Int = 1

        override fun left(): T = _1

        override fun right(): T = _1

        override fun addLeft(t: T): Affix<T> = Two(t, _1)

        override fun addRight(t: T): Affix<T> = Two(_1, t)

        override fun removeLeft(): Affix<T> = throw IllegalStateException("Can't remove left element from One")

        override fun removeRight(): Affix<T> = throw IllegalStateException("Can't remove right element from One")
    }

    private class Two<T>(val _1: T, val _2: T): Affix<T>() {

        override fun size(): Int = 2

        override fun left(): T = _1

        override fun right(): T = _2

        override fun addLeft(t: T): Affix<T> = Three(t, _1, _2)

        override fun addRight(t: T): Affix<T> = Three(_1, _2, t)

        override fun removeLeft(): Affix<T> = One(_2)

        override fun removeRight(): Affix<T> = One(_1)
    }

    private class Three<T>(val _1: T, val _2: T, val _3: T): Affix<T>() {

        override fun size(): Int = 3

        override fun left(): T = _1

        override fun right(): T = _3

        override fun addLeft(t: T): Affix<T> = Four(t, _1, _2, _3)

        override fun addRight(t: T): Affix<T> = Four(_1, _2, _3, t)

        override fun removeLeft(): Affix<T> = Two(_2, _3)

        override fun removeRight(): Affix<T> = Two(_1, _2)
    }

    private class Four<T>(val _1: T, val _2: T, val _3: T, val _4: T): Affix<T>() {

        override fun isFull(): Boolean = true

        override fun size(): Int = 4

        override fun left(): T = _1

        override fun right(): T = _4

        override fun addLeft(t: T): Affix<T> = throw IllegalStateException("Can't add an element to left of Four")

        override fun addRight(t: T): Affix<T> = throw IllegalStateException("Can't add an element to right of Four")

        override fun removeLeft(): Affix<T> = Three(_2, _3, _4)

        override fun removeRight(): Affix<T> = Three(_1, _2, _3)
    }

    companion object {

        /**
         * Creates an empty Deque
         *
         * @return The empty Deque
         */
        @JvmStatic
        operator fun <T> invoke(): Deque<T> = Empty()
    }
}
