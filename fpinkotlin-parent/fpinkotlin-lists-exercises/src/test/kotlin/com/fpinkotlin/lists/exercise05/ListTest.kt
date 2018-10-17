package com.fpinkotlin.lists.exercise05


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {
        "tail" {
            forAll(IntListGenerator()) { (first, second) ->
                if (first.isEmpty())
                    second.isEmpty()
                else
                    second.init().toString() ==
                            first.dropLast(1).let { if (it.isEmpty()) "[NIL]" else it.joinToString(", ", "[", ", NIL]") }
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
