package com.fpinkotlin.advancedlisthandling.exercise05


import com.fpinkotlin.common.Result
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

typealias Stack<T> = List<T>

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

class IntListGenerator(min: Int, max: Int): Gen<List<Int>> {

    val gen = Gen.list(Gen.choose(min, max))

    override fun constants(): Iterable<List<Int>> = gen.constants().map { List(*(it.toTypedArray())) }

    override fun random(): Sequence<List<Int>> = gen.random().map { List(*(it.toTypedArray())) }
}
