package com.fpinkotlin.advancedlisthandling.exercise13


import com.fpinkotlin.common.Result
import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import kotlin.math.max

class ListTest: StringSpec() {

    init {

        "getAt" {
            forAll(IntListGenerator(), { (first, second) ->
                val index = if (first.isEmpty()) 0 else IntGenerator(0, max(first.size - 1, 1)).generate()
                val error = Result.failure<Int>("Index out of bound").toString()
                second.getAt(- (index + 1)).toString() ==  error &&
                    second.getAt(first.size + index).toString() == error &&
                    second.getAt(index).toString() == if (first.isEmpty()) error else Result(first[index])
                        .toString()
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
