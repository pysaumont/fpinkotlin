package com.fpinkotlin.advancedtrees.exercise04

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result

class Map<out K: Comparable<@UnsafeVariance K>, V>(private val delegate: Tree<MapEntry<Int, Any>> = Tree()) { // replace Any with the right type

    private fun getAll(key: @UnsafeVariance K): Result<List<Pair<K, V>>> = TODO("getAll")

    operator fun plus(entry: Pair<@UnsafeVariance K, V>): Map<K, V> = TODO("plus")

    operator fun minus(key: @UnsafeVariance K): Map<K, V> = TODO("minus")

    fun contains(key: @UnsafeVariance K): Boolean = TODO("get")

    operator fun get(key: @UnsafeVariance K): Result<Pair<K, V>> = TODO("get")

    fun isEmpty(): Boolean = delegate.isEmpty

    companion object {

        operator fun <K: Comparable<@UnsafeVariance K>, V> invoke(): Map<K, V> = Map()
    }
}
