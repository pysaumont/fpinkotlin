package com.fpinkotlin.recursion.exercise01


tailrec fun add(a: Int, b: Int): Int = if (b == 0) a else add(inc(a), dec(b))

fun add_(a: Int, b: Int): Int {
    var x = a
    var y = b
    while(y != 0) {
        x = inc(x)
        y = dec(y)
    }
    return x
}

fun inc(n: Int) = n + 1
fun dec(n: Int) = n - 1
