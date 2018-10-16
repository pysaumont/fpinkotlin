package com.fpinkotlin.advancedlisthandling.exercise10


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "unZip" {
            forAll(IntListGenerator(), IntListGenerator()) { list1, list2 ->
                val zip = zipWith(list1, list2) { a -> { b: Int -> Pair(a, b) } }
                val result = unzip(zip)
                result.first.toString() ==
                    list1.reverse().drop(list1.length() - result.first.length()).reverse().toString() &&
                    result.second.toString() ==
                    list2.reverse().drop(list2.length() - result.second.length()).reverse().toString()
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
