package com.fpinkotlin.advancedlisthandling.exercise09


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "product" {
            forAll(IntListGenerator(), IntListGenerator()) { list1, list2 ->
                val result = product(list1, list2) { a -> { b: Int -> Pair(a, b) } }
                result.length() == list1.length() * list2.length()
            }
        }
    }
}

class IntListGenerator(private val min: Int = Int.MIN_VALUE, private val max: Int = Int.MAX_VALUE): Gen<List<Int>> {

    override fun constants(): Iterable<List<Int>> =
            Gen.list(Gen.choose(min, max)).constants().map { List(*(it.toTypedArray())) }

    override fun random(): Sequence<List<Int>> =
            Gen.list(Gen.choose(min, max)).random().map { List(*(it.toTypedArray())) }
}
