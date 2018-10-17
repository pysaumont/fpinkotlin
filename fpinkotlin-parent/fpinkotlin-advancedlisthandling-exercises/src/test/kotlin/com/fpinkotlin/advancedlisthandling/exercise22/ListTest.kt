package com.fpinkotlin.advancedlisthandling.exercise22

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec
import java.util.*


class ListTest: StringSpec() {

    private val random = Random()

    init {

        "splitListAt" {
            forAll(IntListGenerator()) { (_, second) ->
                val splitPosition = if (second.length() == 0) 0 else  random.nextInt(second.length())
                val result = second.splitListAt(splitPosition)
                result.flatMap { it }.toString() == second.toString()
            }
        }

        "divide" {
            forAll(IntListGenerator()) { (_, second) ->
                val depth = random.nextInt(8)
                val result = second.divide(depth)
                result.flatMap { it }.toString() == second.toString()
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
