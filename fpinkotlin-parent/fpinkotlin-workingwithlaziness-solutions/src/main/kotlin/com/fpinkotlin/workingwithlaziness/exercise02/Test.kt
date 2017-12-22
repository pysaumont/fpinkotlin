package com.fpinkotlin.workingwithlaziness.exercise02


fun main(args: Array<String>) {
    val first = { true }
    val second = { throw IllegalStateException() }
    println(first() || second())
    println(or(first, second))
}

fun or(a: () -> Boolean, b: () -> Boolean): Boolean = if (a()) true else b()


