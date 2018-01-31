package com.fpinkotlin.advancedtrees.listing02

import com.fpinkotlin.advancedtrees.common.Result

class Map<out K: Comparable<@UnsafeVariance K>, V> {

    operator fun plus(entry: Pair<@UnsafeVariance K, V>): Map<K, V> = TODO()

    operator fun minus(key: @UnsafeVariance K): Map<K, V> = TODO()

    fun contains(key: @UnsafeVariance K): Boolean = TODO()

    fun get(key: @UnsafeVariance K): Result<Pair<K, V>> = TODO()

    fun isEmpty(): Boolean = TODO()

    companion object {
        operator fun invoke(): Map<Nothing, Nothing> = Map()
    }
}
