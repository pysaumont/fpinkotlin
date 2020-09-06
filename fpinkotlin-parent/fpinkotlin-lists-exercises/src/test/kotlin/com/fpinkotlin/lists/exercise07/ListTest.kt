package com.fpinkotlin.lists.exercise07

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec
import java.util.*

class ListTest: StringSpec() {

    init {

        "product" {
            forAll(DoubleListGenerator()) { (first, second) ->
                val product = product(second)
                val second = first.fold(1.0) { a, b -> a * b }
                println("First: $product")
                println("Second: $second")
                Math.abs(product - second) < 0.001
            }
        }
    }
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
