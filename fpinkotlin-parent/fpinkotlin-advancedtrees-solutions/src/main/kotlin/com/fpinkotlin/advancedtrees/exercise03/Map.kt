package com.fpinkotlin.advancedtrees.exercise03

import com.fpinkotlin.advancedtrees.common.List
import com.fpinkotlin.advancedtrees.common.Result
import com.fpinkotlin.advancedtrees.common.getOrElse
import com.fpinkotlin.advancedtrees.common.sequence

class Map<out K: Comparable<@UnsafeVariance K>, V>(private val delegate: Tree<MapEntry<@UnsafeVariance K, V>> = Tree()) {

    operator fun plus(entry: Pair<@UnsafeVariance K, V>): Map<K, V> = Map(delegate + MapEntry(entry))

    operator fun minus(key: @UnsafeVariance K): Map<K, V> = Map(delegate - MapEntry(key))

    fun contains(key: @UnsafeVariance K): Boolean = delegate.contains(MapEntry(key))

    fun get(key: @UnsafeVariance K): Result<MapEntry<@UnsafeVariance K, V>> = delegate[MapEntry(key)]

    fun isEmpty(): Boolean = delegate.isEmpty()

    fun <B> foldLeft(identity: B, f: (B) -> (MapEntry<@UnsafeVariance K, V>) -> B, g: (B) -> (B) -> B): B =
            delegate.foldLeft(identity, { b ->
                { me: MapEntry<K, V> ->
                    f(b)(me)
                }
            }, g)

    fun values(): List<V> =
        sequence(delegate.foldInReverseOrder(List<Result<V>>()) { lst1 ->
            { me ->
                { lst2 ->
                    lst2.concat(lst1.cons(me.value))
                }
            }
        }).getOrElse(List())

    companion object {

        operator fun <K: Comparable<@UnsafeVariance K>, V> invoke(): Map<K, V> = Map()
    }
}
