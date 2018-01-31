package com.fpinkotlin.recursion.exercise03

import java.math.BigInteger

fun fibonacci(number: Int): Int =
        if (number == 0 || number == 1)
            1
        else
            fibonacci(number - 1) + fibonacci(number - 2)


fun fib(x: Int): BigInteger {
    tailrec
    fun fib(val1: BigInteger, val2: BigInteger, x: BigInteger): BigInteger = when {
        (x == BigInteger.ZERO) -> BigInteger.ONE
        (x == BigInteger.ONE) -> val1 + val2
        else -> fib(val2, val1 + val2, x - BigInteger.ONE)
    }
    return fib(BigInteger.ONE, BigInteger.ZERO, BigInteger.valueOf(x.toLong()))
}
