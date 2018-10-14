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
    }
}

class IntListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100): Gen<Pair<Array<Int>, List<Int>>> {
    override fun constants(): Iterable<Pair<Array<Int>, List<Int>>> = listOf(Pair(arrayOf(), List()))

    override fun random(): Sequence<Pair<Array<Int>, List<Int>>> =
            list(Gen.int(), minLength, maxLength)
                    .map { Pair(it.toTypedArray(),
                            it.fold(List<Int>()) { list, i ->
                                List.cons(i, list)}) }.random()
}
