package com.fpinkotlin.advancedlisthandling.exercise15


import io.kotlintest.properties.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import java.util.*
import kotlin.math.max

class ListTest: StringSpec() {

    private val random = Random()

    init {

        "splitAt" {
            forAll(IntListGenerator()) { (first, second) ->
                val index = if (first.isEmpty()) 0 else random.nextInt(max(first.size - 1, 1))
                val result = second.splitAt(index)
                second.toString() == result.first.concat(result.second).toString()
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
