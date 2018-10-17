package com.fpinkotlin.lists.exercise04


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "dropWhile" {
            forAll(IntListGenerator()) { (first, second) ->
                if (first.isEmpty())
                    second.dropWhile{it > 0}.isEmpty()
                else
                    second.dropWhile{it > 0}.toString() ==
                        first.dropWhile { it > 0 }.let { if (it.isEmpty()) "[NIL]" else it.joinToString(", ", "[", ", NIL]") }
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
