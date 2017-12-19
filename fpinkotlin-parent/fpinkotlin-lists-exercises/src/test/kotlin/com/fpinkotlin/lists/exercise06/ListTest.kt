package com.fpinkotlin.lists.exercise06

import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "sum0" {
            forAll(IntListGenerator(0, 0), { (first, second) ->
                sum(second) == first.sum()
            }, 1)
        }

        "sum" {
            forAll(IntListGenerator(), { (first, second) ->
                sum(second) == first.sum()
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
