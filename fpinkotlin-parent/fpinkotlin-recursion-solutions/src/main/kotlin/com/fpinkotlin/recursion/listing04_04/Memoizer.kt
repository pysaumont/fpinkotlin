package com.fpinkotlin.recursion.listing04_04

import java.util.concurrent.ConcurrentHashMap


class Memoizer<T, U> private constructor() {

    private val cache = ConcurrentHashMap<T, U>()

    private fun doMemoize(function: (T) -> U):  (T) -> U =
        { input ->
            cache.computeIfAbsent(input, {  // <1>
                function(it)
            })
        }

    companion object {

        fun <T, U> memoize(function: (T) -> U): (T) -> U = // <2>
            Memoizer<T, U>().doMemoize(function)
    }
}

fun longComputation(number: Int): Int {
    Thread.sleep(1000)
    return number
}

val f3m = Memoizer.memoize { x: Int ->
    Memoizer.memoize { y: Int ->
        Memoizer.memoize { z: Int ->
            longComputation(z) - (longComputation(y) + longComputation(x)) } } }

fun main(args: Array<String>) {
    val startTime1 = System.currentTimeMillis()
    val result1 = f3m(41)(42)(43)
    val time1 = System.currentTimeMillis() - startTime1
    val startTime2 = System.currentTimeMillis()
    val result2 = f3m(41)(42)(43)
    val time2 = System.currentTimeMillis() - startTime2
    println("First call to memoized function: result = $result1, time = $time1")
    println("Second call to memoized function: result = $result2, time = $time2")
}
