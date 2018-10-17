package com.fpinkotlin.lists.exercise17


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec
import java.util.*

class ListTest: StringSpec() {

    init {

        "doubleToString" {
            forAll(DoubleListGenerator()) { (array, list) ->
                doubleToString(list).toString() == if (array.isEmpty()) "[NIL]" else array.joinToString(", ", "[", ", NIL]")
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
