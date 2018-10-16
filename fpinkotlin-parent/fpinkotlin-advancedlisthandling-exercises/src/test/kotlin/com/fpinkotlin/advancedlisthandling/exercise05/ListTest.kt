package com.fpinkotlin.advancedlisthandling.exercise05


import com.fpinkotlin.common.Result
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec


class ListTest: StringSpec() {

    init {

        "flattenResult" {
            forAll(IntListGenerator(1, 1_000)) { list ->
                val firstFiltered = list.filter { it % 2 == 0 }
                val result = flattenResult(list.map { if (it % 2 == 0) Result(it) else Result.failure("Odd") })
                result.toString() == firstFiltered.toString()
            }
        }
    }
}

class IntListGenerator(private val min: Int, private val max: Int): Gen<List<Int>> {

    override fun constants(): Iterable<List<Int>> = Gen.list(Gen.choose(min, max)).constants().map { List(*(it.toTypedArray())) }

    override fun random(): Sequence<List<Int>> = Gen.list(Gen.choose(min, max)).random().map { List(*(it.toTypedArray())) }
}
