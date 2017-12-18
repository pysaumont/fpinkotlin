package com.fpinkotlin.recursion.exercise05

fun <T> List<T>.head(): T =
        if (this.isEmpty())
            throw IllegalArgumentException("head called on empty list")
        else
            this[0]

fun <T> List<T>.tail(): List<T> =
        if (this.isEmpty())
            throw IllegalArgumentException("tail called on empty list")
        else
            this.subList(1, this.size)

fun <T, U> foldLeft(list: List<T>, z: U, f: (U, T) -> U): U = TODO("foldLeft")

fun sum(list: List<Int>): Int = TODO("sum")

fun string(list: List<Char>): String = TODO("string")
