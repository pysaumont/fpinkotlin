package com.fpinkotlin.recursion.exercise18

import java.math.BigInteger


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

fun <T, U> foldLeft(list: List<T>, z: U, f: (U, T) -> U): U {
    tailrec fun foldLeft(list: List<T>, acc: U, f: (U, T) -> U): U =
        if (list.isEmpty())
            acc
        else
            foldLeft(list.tail(), f(acc, list.head()), f)
    return foldLeft(list, z, f)
}

fun <T, U> map(list: List<T>, f: (T) -> U): List<U> =
    foldLeft(list, listOf()) { acc, elem -> acc + f(elem)}

fun <T> iterate(seed: T, f: (T) -> T, n: Int): List<T> {
    tailrec fun iterate(acc: List<T>, seed: T): List<T> =
        if (acc.size < n)
            iterate(acc + seed, f(seed))
        else
            acc
    return iterate(listOf(), seed)
}

fun <T> makeString(list: List<T>, separator: String): String =
    when {
        list.isEmpty() -> ""
        list.tail().isEmpty() -> list.head().toString()
        else -> list.head().toString() + // <4>
            foldLeft(list.tail(), "") { x, y -> x + separator + y}
    }

fun fiboCorecursive(number: Int): String {
    val seed = Pair(BigInteger.ZERO, BigInteger.ONE)
    val f = { x: Pair<BigInteger, BigInteger> -> Pair(x.second, x.first + x.second) }
    val listOfPairs = iterate(seed, f, number + 1)
    val list = map(listOfPairs) { p -> p.first }
    return makeString(list, ", ")
}

fun main(args: Array<String>) {
    val cache = mutableMapOf<Int, Int>()
    fun double(x: Int) =
        if (cache.containsKey(x)) { // <2>
            println("from cache")
            cache[x] // <3>
        } else {
            println("not in cache")
            val result = x * 2 // <4>
            cache.put(x, result) // <5>
            result // <6>
        }
    println(double(10))
    println(double(10))
}