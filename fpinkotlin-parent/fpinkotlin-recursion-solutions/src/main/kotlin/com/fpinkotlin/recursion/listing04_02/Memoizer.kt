package com.fpinkotlin.recursion.listing04_02

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
