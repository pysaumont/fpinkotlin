package com.fpinkotlin.lists.exercise17


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "doubleToString" {
            forAll(DoubleListGenerator(), { (array, list) ->
                doubleToString(list).toString() == if (array.isEmpty()) "[NIL]" else array.joinToString(", ", "[", ", NIL]")
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
