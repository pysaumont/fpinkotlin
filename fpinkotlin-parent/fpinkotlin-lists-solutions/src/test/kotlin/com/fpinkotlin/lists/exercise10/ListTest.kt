package com.fpinkotlin.lists.exercise10


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec
import java.util.*

class ListTest: StringSpec() {

    init {

        "foldLeft" {
            forAll(IntListGenerator()) { (_, second) ->
                second.foldLeft(0) { a -> { b -> a + b } } ==  second.foldRight(0) { a -> { b -> a + b } }
            }
        }

        "length" {
            forAll(com.fpinkotlin.lists.exercise08.IntListGenerator()) { (first, second) ->
                second.length() == first.size
            }
        }

        "sum" {
            forAll(IntListGenerator()) { (first, second) ->
                sum(second) == first.sum()
            }
        }

        "product" {
            forAll(DoubleListGenerator()) { (first, second) ->
                Math.abs(product(second) - first.fold(1.0) { a, b -> a * b }) < 0.001
            }
        }
    }
}

class IntListGenerator(private val min: Int = Int.MIN_VALUE, private val max: Int = Int.MAX_VALUE): Gen<Pair<Array<Int>, List<Int>>> {

    override fun constants(): Iterable<Pair<Array<Int>, List<Int>>> =
        Gen.list(Gen.choose(min, max)).constants().map { list -> list.toTypedArray().let { Pair(it, List(*(it))) } }

    override fun random(): Sequence<Pair<Array<Int>, List<Int>>> =
        Gen.list(Gen.choose(min, max)).random().map { list -> list.toTypedArray().let { Pair(it, List(*(it))) } }
}

class DoubleListGenerator(val min: Double = Double.MIN_VALUE, val max: Double = Double.MAX_VALUE): Gen<Pair<Array<Double>, List<Double>>> {

    private fun choose(): Gen<Double> {
        assert(min < max) { "min must be < max" }
        val random = Random()
        return object : Gen<Double> {

            override fun constants(): Iterable<Double> = emptyList()

            override fun random(): Sequence<Double> =
                generateSequence { random.nextDouble() }.filter { it in min..max }
        }
    }

    private inline fun <reified T> toPair(list: Collection<T>): Pair<Array<T>, List<T>> =
        list.toTypedArray().let {
            Pair(it, List(*(it)))
        }

    override fun constants(): Iterable<Pair<Array<Double>, List<Double>>> =
        Gen.list(choose()).constants().map { toPair(it) }

    override fun random(): Sequence<Pair<Array<Double>, List<Double>>> =
        Gen.list(choose()).random().map { toPair(it) }
}
