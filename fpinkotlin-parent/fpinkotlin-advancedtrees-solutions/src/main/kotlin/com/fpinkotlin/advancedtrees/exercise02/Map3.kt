package com.fpinkotlin.advancedtrees.exercise02

import com.fpinkotlin.common.Result

class Map3<out K: Comparable<@UnsafeVariance K>, V>(internal val delegate: Tree3<MapEntry<@UnsafeVariance K, V>> = Tree3.empty()) {

    operator fun plus(entry: Pair<@UnsafeVariance K, V>): Map3<K, V> = Map3(delegate.insert(MapEntry(entry)))

    operator fun minus(key: @UnsafeVariance K): Map3<K, V> = Map3(delegate.delete(MapEntry(key)))

    fun contains(key: @UnsafeVariance K): Boolean = delegate.member(MapEntry(key))

    fun get(key: @UnsafeVariance K): Result<MapEntry<@UnsafeVariance K, V>> = delegate[MapEntry(key)]

    fun isEmpty(): Boolean = delegate.isEmpty

    fun size() = delegate.size()

    override fun toString() = delegate.toString()

    companion object {

        operator fun <K: Comparable<@UnsafeVariance K>, V> invoke(): Map3<K, V> = Map3()
    }
}
