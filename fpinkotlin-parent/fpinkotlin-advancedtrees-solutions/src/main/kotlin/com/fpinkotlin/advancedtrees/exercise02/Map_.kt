package com.fpinkotlin.advancedtrees.exercise02

import com.fpinkotlin.advancedTree_s.exercise02.Tree_
import com.fpinkotlin.common.Result

class Map_<out K: Comparable<@UnsafeVariance K>, V>(private val delegate: Tree_<MapEntry<@UnsafeVariance K, V>> = Tree_()) {

//    val size = delegate.size

    operator fun plus(entry: Pair<@UnsafeVariance K, V>): Map_<K, V> = Map_(delegate + (MapEntry(entry)))

    operator fun minus(key: @UnsafeVariance K): Map_<K, V> = Map_(delegate.delete(MapEntry(key)))

    fun contains(key: @UnsafeVariance K): Boolean = delegate.contains(MapEntry(key))

    fun get(key: @UnsafeVariance K): Result<MapEntry<@UnsafeVariance K, V>> = delegate[MapEntry(key)]

    fun isEmpty(): Boolean = delegate.isEmpty()

    override fun toString() = delegate.toString()

    companion object {

        operator fun <K: Comparable<@UnsafeVariance K>, V> invoke(): Map_<K, V> = Map_()
    }
}
