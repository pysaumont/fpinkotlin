package com.fpinkotlin.recursion.exercise16


fun <T> iterate(seed: T, f: (T) -> T, n: Int): List<T> {
    tailrec fun iterate(acc: List<T>, seed: T): List<T> =
        if (acc.size < n)
            iterate(acc + seed, f(seed))
        else
            acc
    return iterate(listOf(), seed)
}
