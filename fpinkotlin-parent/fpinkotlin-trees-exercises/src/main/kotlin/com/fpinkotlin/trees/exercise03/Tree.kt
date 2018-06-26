package com.fpinkotlin.trees.exercise03

import com.fpinkotlin.common.List

sealed class Tree<out A: Comparable<@UnsafeVariance A>> {

    abstract fun isEmpty(): Boolean

    operator fun plus(a: @UnsafeVariance A): Tree<A> = when (this) {
        Empty -> T(Empty, a, Empty)
        is T -> when {
            a < this.value -> T(left + a, this.value, right)
            a > this.value -> T(left, this.value, right + a)
            else -> T(this.left, a, this.right)
        }
    }

    fun contains(a: @UnsafeVariance A): Boolean = TODO("contains")

    internal object Empty : Tree<Nothing>() {

        override fun isEmpty(): Boolean = true

        override fun toString(): String = "E"
    }

    internal class T<out A: Comparable<@UnsafeVariance A>>(internal val left: Tree<A>,
                                                           internal val value: A,
                                                           internal val right: Tree<A>) : Tree<A>() {

        override fun isEmpty(): Boolean = false

        override fun toString(): String = "(T $left $value $right)"
    }

    companion object {

        operator fun <A: Comparable<A>> invoke(): Tree<A> = Empty

        operator fun <A: Comparable<A>> invoke(vararg az: A): Tree<A> =
            az.fold(Empty) { tree: Tree<A>, a: A -> tree.plus(a) }

        operator fun <A: Comparable<A>> invoke(list: List<A>): Tree<A> =
            list.foldLeft(Empty as Tree<A>) { tree: Tree<A> -> { a: A -> tree.plus(a) } }
    }
}

fun main(args: Array<String>) {
    val t1 = Tree<Int>() + 5 + 2 + 8
    println(t1)
    val t2: Tree<Int> = Tree(5, 3, 7, 2, 8, 1, 4, 6)
    println(t2)
    val t3: Tree<Parent> = Tree<Parent>() + Parent(5) + Clazz(2) + Child(8)
    println(t3)
    val t4: Tree<Parent> = Tree<Clazz>() + Child(5) + Clazz(2) + Clazz(8)
    println(t4)
    val t5: Tree<Number> = Tree<Number>() + Number(5) + Integer(2) + Number(8)
    println(t5)
    val t6: Tree<Integer> = Tree<Integer>() + Integer(5) + Integer(2) + Integer(8)
    println(t6)

    println(Number(5) < Integer(2))
    println(Integer(5) < Number(2))
}

open class Parent(val value: Int): Comparable<Parent> {
    override fun compareTo(other: Parent): Int = value.compareTo(other.value)
    override fun toString() = value.toString()
}

open class Clazz(value: Int): Parent(value)

class Child(value: Int): Clazz(value)

open class Number(val value: Int): Comparable<Number> {
    override fun compareTo(other: Number): Int = value.compareTo(other.value)
    override fun toString() = value.toString()
}

class Integer(value: Int): Number(value)