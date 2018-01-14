package com.fpinkotlin.advancedtrees.exercise04

import com.fpinkotlin.advancedtrees.common.Result

class MapEntry<K: Any, V> private constructor(val key: K, val value: Result<V>): Comparable<MapEntry<K, V>> {

    override fun compareTo(other: MapEntry<K, V>): Int = hashCode().compareTo(other.hashCode())

    override fun toString(): String = "MapEntry($key, $value)"

    override fun equals(other: Any?): Boolean =
            this === other || when (other) {
                is MapEntry<*, *> -> key == other.key
                else -> false
            }

    override fun hashCode(): Int = key.hashCode()

    companion object {

        fun <K: Comparable<K>, V> of(key: K, value: V): MapEntry<K, V> =
                MapEntry(key, Result(value))

        operator fun <K: Comparable<K>, V> invoke(pair: Pair<K, V>): MapEntry<K, V> =
                MapEntry(pair.first, Result(pair.second))

        operator fun <K: Comparable<K>, V> invoke(key: K): MapEntry<K, V> =
                MapEntry(key, Result())
    }
}