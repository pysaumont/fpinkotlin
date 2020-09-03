package com.fpinkotlin.recursion.exercise03

import java.math.BigInteger


fun fib(x: Int): BigInteger {
    tailrec fun fibTail(val1: BigInteger, val2: BigInteger, x: BigInteger): BigInteger =
            when {
                (x == BigInteger.ZERO) -> BigInteger.ONE
                (x == BigInteger.ONE) -> val1 + val2
                else -> fibTail(val2, val1 + val2, x - BigInteger.ONE)
            }
    return fibTail(BigInteger.ZERO, BigInteger.ONE, BigInteger.valueOf(x.toLong()))
}
