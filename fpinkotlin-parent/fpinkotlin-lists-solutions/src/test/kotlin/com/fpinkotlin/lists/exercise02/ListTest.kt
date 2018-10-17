package com.fpinkotlin.lists.exercise02

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import java.util.*

class ListTest: StringSpec() {

    private val random = Random()

    init {

        "setHead" {
            val i = random.nextInt()
            forAll(IntListGenerator()) { (first, second) ->
                second.isEmpty() ||
                second.setHead(i).toString() ==
                        "[$i, ${first.joinToString(", ", "", ", NIL]").substringAfter(", ")}"
            }
        }

        "setHeadEmpty" {
            val list = List<Int>()
            shouldThrow<IllegalStateException> { list.setHead(1) }
        }
    }
}

class IntListGenerator(private val min: Int = Int.MIN_VALUE, private val max: Int = Int.MAX_VALUE): Gen<Pair<Array<Int>, List<Int>>> {

    override fun constants(): Iterable<Pair<Array<Int>, List<Int>>> =
        Gen.list(Gen.choose(min, max)).constants().map { list -> list.toTypedArray().let { Pair(it, List(*(it))) } }

    override fun random(): Sequence<Pair<Array<Int>, List<Int>>> =
        Gen.list(Gen.choose(min, max)).random().map { list -> list.toTypedArray().let { Pair(it, List(*(it))) } }
}
