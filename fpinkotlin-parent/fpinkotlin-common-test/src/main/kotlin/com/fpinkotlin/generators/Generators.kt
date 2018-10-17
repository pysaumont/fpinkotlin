package com.fpinkotlin.generators

import io.kotlintest.properties.Gen
import java.util.*

typealias Stack<A> = com.fpinkotlin.common.List<A>

class IntListGenerator(private val min: Int = Int.MIN_VALUE, private val max: Int = Int.MAX_VALUE): Gen<Pair<Array<Int>, Stack<Int>>> {

    override fun constants(): Iterable<Pair<Array<Int>, Stack<Int>>> =
        Gen.list(Gen.choose(min, max)).constants().map { list -> list.toTypedArray().let { Pair(it, Stack(*(it))) } }

    override fun random(): Sequence<Pair<Array<Int>, Stack<Int>>> =
        Gen.list(Gen.choose(min, max)).random().map { list -> list.toTypedArray().let { Pair(it, Stack(*(it))) } }
}

class DoubleListGenerator(val min: Double = Double.MIN_VALUE, val max: Double = Double.MAX_VALUE): Gen<Pair<Array<Double>, Stack<Double>>> {

    private fun choose(): Gen<Double> {
        assert(min < max) { "min must be < max" }
        val random = Random()
        return object : Gen<Double> {

            override fun constants(): Iterable<Double> = emptyList()

            override fun random(): Sequence<Double> =
                generateSequence { random.nextDouble() }.filter { it in min..max }
        }
    }

    private inline fun <reified T> toPair(list: Collection<T>): Pair<Array<T>, Stack<T>> =
        list.toTypedArray().let {
            Pair(it, Stack(*(it)))
        }

    override fun constants(): Iterable<Pair<Array<Double>, Stack<Double>>> =
        Gen.list(choose()).constants().map { toPair(it) }

    override fun random(): Sequence<Pair<Array<Double>, Stack<Double>>> =
        Gen.list(choose()).random().map { toPair(it) }
}

class CharKListGenerator: Gen<List<Char>> {

    override fun constants(): Iterable<List<Char>> =
            Gen.list(Gen.choose(32, 127)).constants().map { list -> list.map(Int::toChar) }

    override fun random(): Sequence<List<Char>> =
            Gen.list(Gen.choose(32, 127)).random().map { list -> list.map(Int::toChar) }
}
