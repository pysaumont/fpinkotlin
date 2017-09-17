package com.fpinkotlin.recursion.exercise03

import java.math.BigInteger

fun fibonacci(number: Int): Int =
        if (number == 0 || number == 1)
            1
        else
            fibonacci(number - 1) + fibonacci(number - 2)


fun main(args: Array<String>) {
    (0 until 10).forEach { print("${fibonacci(it)} ") }
}

fun fib(x: Int): BigInteger {
    tailrec
    fun fib_(acc1: BigInteger, acc2: BigInteger, x: BigInteger): BigInteger = when {
        (x == BigInteger.ZERO) -> BigInteger.ONE
        (x == BigInteger.ONE) -> acc1 + acc2
        else -> fib_(acc2, acc1 + acc2, x - BigInteger.ONE)
    }
    return fib_(BigInteger.ONE, BigInteger.ZERO, BigInteger.valueOf(x.toLong()))
}
