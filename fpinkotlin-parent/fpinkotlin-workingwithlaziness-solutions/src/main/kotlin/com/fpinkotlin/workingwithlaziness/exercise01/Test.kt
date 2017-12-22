package com.fpinkotlin.workingwithlaziness.exercise01



//fun main(args: Array<String>) {
//    val x = getValue()
//}
//
//fun getValue(): Int {
//    println("Returning 5")
//    return 5
//}

fun main(args: Array<String>) {
    println(or(true, true))
    println(or(true, false))
    println(or(false, true))
    println(or(false, false))

    println(and(true, true))
    println(and(true, false))
    println(and(false, true))
    println(and(false, false))
}

fun or(a: Boolean, b: Boolean): Boolean {
    return if (a) true else b
}

fun and(a: Boolean, b: Boolean): Boolean {
    return if (a) b else false
}

