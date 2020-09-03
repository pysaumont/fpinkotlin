package com.fpinkotlin.recursion.exercise16


fun <T> iterate(seed: T, f: (T) -> T, n: Int): List<T> {
    tailrec fun iterateTail(acc: List<T>, elem: T): List<T> =
            if (acc.size == n)
                acc
            else iterateTail(acc + elem, f(elem))
    return iterateTail(listOf(), seed)
}
