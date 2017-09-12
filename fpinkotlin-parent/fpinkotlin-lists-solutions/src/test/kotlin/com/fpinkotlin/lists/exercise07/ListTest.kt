package com.fpinkotlin.lists.exercise07

import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "product0" {
            forAll(DoubleListGenerator(0, 0), { (first, second) ->
                Math.abs(product(second) - first.fold(1.0) { a, b -> a * b }) < 0.001
            }, 1)
        }

        "product" {
            forAll(DoubleListGenerator(), { (first, second) ->
                Math.abs(product(second) - first.fold(1.0) { a, b -> a * b }) < 0.001
            })
        }
    }
}

class DoubleListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100) : Gen<Pair<Array<Double>,
    List<Double>>> {

    override fun generate(): Pair<Array<Double>, List<Double>> {
        val array: Array<Double> = list(Gen.double(), minLength, maxLength).generate().toTypedArray()
        return Pair(array, List(*array))
    }
}
