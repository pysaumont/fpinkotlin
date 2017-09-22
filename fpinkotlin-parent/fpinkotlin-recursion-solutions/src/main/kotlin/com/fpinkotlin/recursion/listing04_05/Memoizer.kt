package com.fpinkotlin.recursion.listing04_05

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

data class Tuple4<out T, out U, out V, out W>(val _1: T, val _2: U, val _3: V, val _4: W)

val ft = { (a, b, c, d): Tuple4<Int, Int, Int, Int> ->
    longComputation(a) + longComputation(b) - longComputation(c) * longComputation(d) }

val ftm = Memoizer.memoize(ft)

fun main(args: Array<String>) {
    val startTime1 = System.currentTimeMillis()
    val result1 = ftm(Tuple4(40, 41, 42, 43))
    val time1 = System.currentTimeMillis() - startTime1
    val startTime2 = System.currentTimeMillis()
    val result2 = ftm(Tuple4(40, 41, 42, 43))
    val time2 = System.currentTimeMillis() - startTime2
    println("First call to memoized function: result = $result1, time = $time1")
    println("Second call to memoized function: result = $result2, time = $time2")
}
