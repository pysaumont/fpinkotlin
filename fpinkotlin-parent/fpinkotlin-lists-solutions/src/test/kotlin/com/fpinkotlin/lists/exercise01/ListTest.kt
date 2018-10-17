package com.fpinkotlin.lists.exercise01

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "cons" {
            forAll(IntListGenerator(), Gen.int()) { (first, second), i ->
                second.cons(i).toString() == first.let { if (it.isEmpty()) "[$i, NIL]" else it.joinToString(", ", "[$i, ", ", NIL]") }
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
