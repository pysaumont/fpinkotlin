package com.fpinkotlin.advancedlisthandling.exercise09


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "product" {
            forAll(IntListPairGenerator()) { (list1, list2) ->
                val result = product(list1, list2) { a -> { b: Int -> Pair(a, b) } }
                result.length() == list1.length() * list2.length()
            }
        }
    }
}

class IntListPairGenerator: Gen<Pair<List<Int>, List<Int>>> {

    override fun constants(): Iterable<Pair<List<Int>, List<Int>>> =
            IntListGenerator().constants().zipWithNext()

    override fun random(): Sequence<Pair<List<Int>, List<Int>>> =
            IntListGenerator().random().zipWithNext()
}

class IntListGenerator(private val min: Int = Int.MIN_VALUE, private val max: Int = Int.MAX_VALUE): Gen<List<Int>> {

    override fun constants(): Iterable<List<Int>> =
            Gen.list(Gen.choose(min, max)).constants().map { List(*(it.toTypedArray())) }

    override fun random(): Sequence<List<Int>> =
            Gen.list(Gen.choose(min, max)).random().map { List(*(it.toTypedArray())) }
}
