package com.fpinkotlin.recursion.listing02

import java.util.concurrent.ConcurrentHashMap


class Memoizer<T, U> private constructor() {

    private val cache = ConcurrentHashMap<T, U>()

    private fun doMemoize(function: (T) -> U):  (T) -> U =
        { input ->
            cache.computeIfAbsent(input) {
                function(it)
            }
        }

    companion object {

        fun <T, U> memoize(function: (T) -> U): (T) -> U =
            Memoizer<T, U>().doMemoize(function)
    }
}
