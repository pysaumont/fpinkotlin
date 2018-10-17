package com.fpinkotlin.advancedlisthandling.exercise17


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "groupBy" {
            forAll(IntListGenerator()) { (first, second) ->
                val result = second.groupBy { it % 5 }
                result.values.fold(0) { r, list -> r + list.length() } == first.size
            }
        }
    }
}

class IntListGenerator(private val min: Int = Int.MIN_VALUE, private val max: Int = Int.MAX_VALUE): Gen<Pair<Array<Int>, List<Int>>> {

    override fun constants(): Iterable<Pair<Array<Int>, List<Int>>> =
            Gen.list(Gen.choose(min, max)).constants().map { list -> list.toTypedArray().let { Pair(it, List(*(it))) } }

    override fun random(): Sequence<Pair<Array<Int>, List<Int>>> =
            Gen.list(Gen.choose(min, max)).random().map { list -> list.toTypedArray().let { Pair(it, List(*(it))) } }
}
