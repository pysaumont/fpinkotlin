package com.fpinkotlin.advancedtrees.exercise02

import com.fpinkotlin.advancedTreeOlds.exercise02.TreeOld
import com.fpinkotlin.common.Result

class MapOld<out K: Comparable<@UnsafeVariance K>, V>(private val delegate: TreeOld<MapEntry<@UnsafeVariance K, V>> = TreeOld()) {

    operator fun plus(entry: Pair<@UnsafeVariance K, V>): MapOld<K, V> = MapOld(delegate + MapEntry(entry))

    operator fun minus(key: @UnsafeVariance K): MapOld<K, V> = MapOld(delegate - MapEntry(key))

    fun contains(key: @UnsafeVariance K): Boolean = delegate.contains(MapEntry(key))

    fun get(key: @UnsafeVariance K): Result<MapEntry<@UnsafeVariance K, V>> = delegate[MapEntry(key)]

    fun isEmpty(): Boolean = delegate.isEmpty()

    fun size() = delegate.size

    override fun toString() = delegate.toString()

    companion object {

        operator fun <K: Comparable<@UnsafeVariance K>, V> invoke(): MapOld<K, V> = MapOld()
    }
}
