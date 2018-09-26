package com.fpinkotlin.advancedlisthandling.exercise17


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "groupBy" {
            forAll(IntListGenerator(), { (first, second) ->
                val result = second.groupBy { it % 5 }
                result.values.fold(0) { r, list -> r + list.length() } == first.size
            })
        }
        "groupByViaFoldRight" {
            forAll(IntListGenerator(), { (first, second) ->
                val result = second.groupByViaFoldRight { it % 5 }
                result.values.fold(0) { r, list -> r + list.length() } == first.size
            })
        }
    }
}

class IntListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100) : Gen<Pair<Array<Int>, List<Int>>> {

    override fun generate(): Pair<Array<Int>, List<Int>> {
        val array: Array<Int> = list(Gen.int(), minLength, maxLength).generate().toTypedArray()
        return Pair(array, List(*array))
    }
}
