package com.fpinkotlin.trees.exercise11

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.common.getOrElse
import com.fpinkotlin.common.orElse
import kotlin.math.max


sealed class Tree<out A: Comparable<@UnsafeVariance A>> {

    abstract fun <B> foldInOrder(identity: B, f: (B) -> (A) -> (B) -> B): B

    abstract fun <B> foldPreOrder(identity: B, f: (A) -> (B) -> (B) -> B): B

    abstract fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (A) -> B): B

    abstract fun <B> foldLeft(identity: B,
                              f: (B) -> (A) -> B,
                              g: (B) -> (B) -> B): B

    abstract fun toListPreOrderLeft(): List<A>

    abstract fun <B> foldRight(identity: B,
                               f: (A) -> (B) -> B,
                               g: (B) -> (B) -> B): B

    abstract fun merge(tree: Tree<@UnsafeVariance A>): Tree<A>

    abstract fun min(): Result<A>

    abstract fun max(): Result<A>

    abstract fun size(): Int

    abstract fun height(): Int

    abstract fun isEmpty(): Boolean

    operator fun plus(a: @UnsafeVariance A): Tree<A> = plus(this, a)

    fun <B: Comparable<B>> map(f: (A) -> B): Tree<B> =
        foldInOrder(Empty) { t1: Tree<B> ->
            { i: A ->
                { t2: Tree<B> ->
                    Tree(t1, f(i), t2)
                }
            }
        }

    fun <B> foldLeft(identity: B, f: (B) -> (A) -> B): B =
                        toListPreOrderLeft().foldLeft(identity, f)

    fun remove(a: @UnsafeVariance A): Tree<A> = when(this) {
        Empty -> this
        is Tree.T     ->  when {
            a < value -> T(left.remove(a), value, right)
            a > value -> T(left, value, right.remove(a))
            else -> left.removeMerge(right)
        }
    }

    fun removeMerge(ta: Tree<@UnsafeVariance A>): Tree<A> = when (this) {
        Empty -> ta
        is Tree.T     -> when (ta) {
            Empty -> this
            is T -> when {
                ta.value < value -> T(left.removeMerge(ta), value, right)
                ta.value > value -> T(left, value, right.removeMerge(ta))
                else             -> throw IllegalStateException("We shouldn't be here")

            }
        }
    }

    fun contains(a: @UnsafeVariance A): Boolean = when (this) {
        Empty -> false
        is T -> when {
            a < value -> left.contains(a)
            a > value -> right.contains(a)
            else -> value == a
        }
    }

    internal object Empty : Tree<Nothing>() {

        override fun toListPreOrderLeft(): List<Nothing> = List()

        override fun <B> foldInOrder(identity: B, f: (B) -> (Nothing) -> (B) -> B): B = identity

        override fun <B> foldPreOrder(identity: B, f: (Nothing) -> (B) -> (B) -> B): B = identity

        override fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (Nothing) -> B): B = identity

        override fun <B> foldRight(identity: B, f: (Nothing) -> (B) -> B, g: (B) -> (B) -> B): B = identity

        override fun <B> foldLeft(identity: B, f: (B) -> (Nothing) -> B, g: (B) -> (B) -> B): B = identity

        override fun merge(tree: Tree<Nothing>): Tree<Nothing> = tree

        override fun min(): Result<Nothing> = Result()

        override fun max(): Result<Nothing> = Result()

        override fun size(): Int = 0

        override fun height(): Int = -1

        override fun isEmpty(): Boolean = true

        override fun toString(): String = "E"
    }

    internal class T<out A: Comparable<@UnsafeVariance A>>(internal val left: Tree<A>,
                                                           internal val value: A,
                                                           internal val right: Tree<A>) : Tree<A>() {

        override fun toListPreOrderLeft(): List<A> =
            left.toListPreOrderLeft().concat(right.toListPreOrderLeft()).cons(value)

        override fun <B> foldInOrder(identity: B, f: (B) -> (A) -> (B) -> B): B =
            f(left.foldInOrder(identity, f))(value)(right.foldInOrder(identity, f))

        override fun <B> foldPreOrder(identity: B, f: (A) -> (B) -> (B) -> B): B =
            f(value)(left.foldPreOrder(identity, f))(right.foldPreOrder(identity, f))

        override fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (A) -> B): B =
            f(left.foldPostOrder(identity, f))(right.foldPostOrder(identity, f))(value)

        override fun <B> foldLeft(identity: B, f: (B) -> (A) -> B, g: (B) -> (B) -> B): B =
            g(right.foldLeft(identity, f, g))(f(left.foldLeft(identity, f, g))(this.value))

        override fun <B> foldRight(identity: B, f: (A) -> (B) -> B, g: (B) -> (B) -> B): B =
            g(f(this.value)(left.foldRight(identity, f, g)))(right.foldRight(identity, f, g))

        override fun merge(tree: Tree<@UnsafeVariance A>): Tree<A> = when (tree) {
            Empty -> this
            is T ->   when  {
                tree.value > this.value -> T(left, value, right.merge(T(Empty, tree.value, tree.right))).merge(tree.left)
                tree.value < this.value -> T(left.merge(T(tree.left, tree.value, Empty)), value, right).merge(tree.right)
                else                    -> T(left.merge(tree.left), value, right.merge(tree.right))
            }
        }

        override fun min(): Result<A> = left.min().orElse { Result(value) }

        override fun max(): Result<A> = right.max().orElse { Result(value) }

        override fun size(): Int = 1 + left.size() + right.size()

        override fun height(): Int = 1 + max(left.height(), right.height())

        override fun isEmpty(): Boolean = false

        override fun toString(): String = "(T $left $value $right)"
    }

    companion object {

        fun <A: Comparable<A>> plus(tree: Tree<A>, a: A): Tree<A> {
            return when(tree) {
                Empty -> T(tree, a, tree)
                is T -> {
                    when {
                        a < tree.value -> Tree.T(plus(tree.left, a), tree.value, tree.right)
                        a > tree.value -> Tree.T(tree.left, tree.value, plus(tree.right, a))
                        else -> tree
                    }
                }
            }
        }

        operator fun <A: Comparable<A>> invoke(): Tree<A> = Empty

        operator fun <A: Comparable<A>> invoke(vararg az: A): Tree<A> =
            az.fold(Empty, { tree: Tree<A>, a: A -> tree.plus(a) })

        operator fun <A: Comparable<A>> invoke(list: List<A>): Tree<A> =
            list.foldLeft(Empty as Tree<A>, { tree: Tree<A> -> { a: A -> tree.plus(a) } })

        operator fun <A: Comparable<A>> invoke(left: Tree<A>, a: A, right: Tree<A>): Tree<A> =
            when {
                ordered(left, a, right) -> T(left, a, right)
                ordered(right, a, left) -> T(right, a, left)
                else                    -> Tree(a).merge(left).merge(right)
            }

        fun <A: Comparable<A>> lt(first: A, second: A): Boolean = first < second

        fun <A: Comparable<A>> lt(first: A, second: A, third: A): Boolean =
                                       lt(first, second) && lt(second, third)

        fun <A: Comparable<A>> ordered(left: Tree<A>,
                                       a: A, right: Tree<A>): Boolean =
            (left.max().flatMap { lMax ->
                right.min().map { rMin ->
                    lt(lMax, a, rMin)
                }
            }.getOrElse(left.isEmpty() && right.isEmpty()) ||
                left.min()
                    .mapEmpty()
                    .flatMap { _ ->
                                 right.min().map { rMin ->
                                     lt(a, rMin)
                                 }
                     }.getOrElse(false) ||
                right.min()
                    .mapEmpty()
                    .flatMap { _ ->
                                 left.max().map { lMax ->
                                     lt(lMax, a)
                                 }
                             }.getOrElse(false))
    }
}

fun main(args: Array<String>) {

    val tree = Tree(4, 2, 6, 1, 3, 5, 7)
    val result1 = tree.foldInOrder(Tree<Int>(),
                     { t1 -> { i -> { t2 -> Tree(t1, i, t2) } } })
    val result2 = tree.foldPostOrder(Tree<Int>(),
                       { t1 -> { t2 -> { i -> Tree(t1, i, t2) } } })
    val result3 = tree.foldPreOrder(Tree<Int>(),
                      { i -> { t1 -> { t2 -> Tree(t1, i, t2) } } })
    println(tree)
    println(result1)
    println(result2)
    println(result3)
}