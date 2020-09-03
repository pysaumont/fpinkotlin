package com.fpinkotlin.recursion.exercise10


fun <T> unfold(seed: T, f: (T) -> T, p: (T) -> Boolean): List<T> {
    tailrec fun unfoldTail(seed: T, acc: List<T>): List<T> =
            if (!p(seed))
                acc
            else
                unfoldTail(f(seed), acc + seed)
    return unfoldTail(seed, listOf())
}
