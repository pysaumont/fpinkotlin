package com.fpinkotlin.advancedlisthandling.exercise22

import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec


class ListTest: StringSpec() {

    init {

        "splitListAt" {
            forAll(IntListGenerator(), { (first, second) ->
                val splitPosition = if (second.length() == 0) 0 else  IntGenerator(0, second.length()).generate()
                val result = second.splitListAt(splitPosition)
                result.flatMap { it }.toString() == second.toString()
            })
        }

        "divide" {
            forAll(IntListGenerator(), { (first, second) ->
                val depth = IntGenerator(0, 8).generate()
                val result = second.divide(depth)
                result.flatMap { it }.toString() == second.toString()
            })
        }
    }
}

class IntListGenerator(private val minLength: Int = 0, private val maxLength: Int = 1_000) : Gen<Pair<Array<Int>, List<Int>>> {

    override fun generate(): Pair<Array<Int>, List<Int>> {
        val array: Array<Int> = list(IntGenerator(0, 100), minLength, maxLength).generate().toTypedArray()
        return Pair(array, List(*array))
    }
}
