package com.fpinkotlin.trees

import com.fpinkotlin.trees.List.Companion.cons
import com.fpinkotlin.trees.Tree.Companion.add


sealed class List<out A> {

    abstract fun contains(a: @UnsafeVariance A): Boolean

    fun cons(a: @UnsafeVariance A) = cons(a, this)

    internal object Nil: List<Nothing>() {

        override fun contains(a: Nothing): Boolean = false
    }

    internal class Cons<out A>(internal val head: A, internal val tail: List<A>): List<A>() {

        override fun contains(a: @UnsafeVariance A): Boolean = when (a) {
            head -> true
            else -> tail.contains(a)
        }
    }

    companion object {
        fun <A> cons(a: A, list: List<A>): List<A> = Cons(a, list)
    }
}

sealed class Tree<out A: Comparable<@UnsafeVariance A>> {

//    abstract fun contains(a: @UnsafeVariance A): Boolean

    fun add(a: @UnsafeVariance A) = add(this, a)

    internal object Empty: Tree<Nothing>() {

//        override fun contains(a: Nothing): Boolean = false
    }

    internal class T<out A: Comparable<@UnsafeVariance A>>(internal val left: Tree<A>, internal val value: A, internal val right: Tree<A>): Tree<A>() {

//        override fun contains(a: @UnsafeVariance A): Boolean = when (a) {
//            value -> true
//            else -> left.contains(a) || right.contains(a)
//        }
    }

    companion object {
        fun <A: Comparable<@UnsafeVariance A>> add(tree: Tree<A>, a: A): Tree<A> = when (tree) {
            is Empty -> T(Empty as Tree<A>, a, Empty as Tree <A>)
            is T ->
                when {
                a.compareTo(tree.value) < 0 -> Tree.T(add(tree.left, a), tree.value, tree.right)
                a.compareTo(tree.value) > 0 -> Tree.T(tree.left, tree.value, add(tree.right, a))
                else -> tree
            }
        }
    }
}

fun <A: Comparable<@UnsafeVariance A>> Tree<A>.insert(a: A): Tree<A> = when (this) {
    is Tree.Empty -> Tree.T(Tree.Empty as Tree<A>, a, Tree.Empty as Tree<A>)
    is Tree.T<A> ->
        when {
            a.compareTo(value) < 0 -> Tree.T(add(left, a), value, right)
            a.compareTo(value) > 0 -> Tree.T(left, value, add(right, a))
            else -> Tree.T(left, a, right)
        }
}

fun <A: Comparable<@UnsafeVariance A>> Tree<A>.contains(a: A): Boolean = when (this) {
    is Tree.Empty -> false
    is Tree.T -> a == value || left.contains(a) || right.contains(a)
}

fun main(args: Array<String>) {
    val list: List<Int> = cons(3, cons(2, cons(1, List.Nil)))
    println(list.contains(3))
    val tree: Tree<String> = add(add(add(Tree.Empty, "1"), "2"), "3")
    println(tree.contains("3"))
    val tree2: Tree<String> = Tree.Empty.insert("1").insert("2").insert("3")
    println(tree2.contains("3"))

}