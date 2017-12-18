package com.fpinkotlin.advancedlisthandling.exercise16


import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import kotlin.math.max
import kotlin.math.min

class ListTest: StringSpec() {

    init {

        "hasSubList" {
            forAll(IntListGenerator(), { (first, second) ->
                val index = if (first.isEmpty()) 0 else IntGenerator(0, max(first.size - 1, 1)).generate()
                val result = second.splitAt(index)
                second.hasSubList(result.first) &&
                    second.hasSubList(result.second) &&
                    second.hasSubList(result.second.drop(min(result.second.length(), 2))) &&
                    !second.hasSubList(result.second.drop(min(result.second.length(), 2)).cons(-1))
            })
        }
    }
}

class IntListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100) : Gen<Pair<Array<Int>, List<Int>>> {

    override fun generate(): Pair<Array<Int>, List<Int>> {
        val array: Array<Int> = list(IntGenerator(0, Int.MAX_VALUE), minLength, maxLength).generate().toTypedArray()
        return Pair(array, List(*array))
    }
}
