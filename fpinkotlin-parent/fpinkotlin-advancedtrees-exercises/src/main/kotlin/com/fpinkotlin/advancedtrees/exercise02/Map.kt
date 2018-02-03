package com.fpinkotlin.advancedtrees.exercise02

import com.fpinkotlin.common.Result

class Map<out K: Comparable<@UnsafeVariance K>, V> {

    operator fun plus(entry: Pair<@UnsafeVariance K, V>): Map<K, V> = TODO("plus")

    operator fun minus(key: @UnsafeVariance K): Map<K, V> = TODO("minus")

    fun contains(key: @UnsafeVariance K): Boolean = TODO("contains")

    operator fun get(key: @UnsafeVariance K): Result<MapEntry<@UnsafeVariance K, V>> = TODO("get")

    fun isEmpty(): Boolean = TODO("isEmpty")

    fun size(): Int = TODO("size")

    override fun toString() = TODO("plus")

    companion object {

        operator fun <K: Comparable<@UnsafeVariance K>, V> invoke(): Map<K, V> = Map()
    }
}
