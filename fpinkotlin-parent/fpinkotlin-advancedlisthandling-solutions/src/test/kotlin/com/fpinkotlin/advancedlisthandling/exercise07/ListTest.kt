package com.fpinkotlin.advancedlisthandling.exercise07


import com.fpinkotlin.common.Result
import io.kotlintest.properties.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import java.util.*

class ListTest: StringSpec() {

    init {

        "sequence" {
            forAll(DoubleListGenerator()) { list ->
                val errorMessage = "div by 0"
                val f = { x: Double ->  if (x != 0.0) 1 / x else throw IllegalArgumentException(errorMessage) }
                val result = sequence(list.map { Result.of{ f(it) } })
                result.toString() == if (product(list) == 0.0)
                    Result.failure<Double>(errorMessage).toString()
                else Result(list.map(f)).toString()
            }
        }
    }
}

class DoubleListGenerator(val min: Double = Double.MIN_VALUE, val max: Double = Double.MAX_VALUE): Gen<List<Double>> {

    private fun choose(): Gen<Double> {
        assert(min < max) { "min must be < max" }
        val random = Random()
        return object : Gen<Double> {

            override fun constants(): Iterable<Double> = emptyList()

            override fun random(): Sequence<Double> =
                    generateSequence { random.nextDouble() }.filter { it in min..max }
        }
    }

    override fun constants(): Iterable<List<Double>> = Gen.list(choose()).constants().map { List(*(it.toTypedArray())) }

    override fun random(): Sequence<List<Double>> = Gen.list(choose()).random().map { List(*(it.toTypedArray())) }
}
