package com.fpinkotlin.advancedlisthandling.exercise05


import com.fpinkotlin.common.Result
import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "flattenResult" {
            forAll(IntListGenerator(), { (first, second) ->
                val firstFiltered = List(*first).filter { it % 2 == 0 }
                val result = flattenResult(second.map { if (it % 2 == 0) Result(it) else Result.failure("Odd") })
                result.toString() == firstFiltered.toString()
            })
        }

        "flattenResultLeft" {
            forAll(IntListGenerator(), { (first, second) ->
                val firstFiltered = List(*first).filter { it % 2 == 0 }
                val result = flattenResultLeft(second.map { if (it % 2 == 0) Result(it) else Result.failure("Odd") })
                result.toString() == firstFiltered.toString()
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
