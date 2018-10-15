package com.fpinkotlin.advancedlisthandling.exercise16


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec
import java.util.*
import kotlin.math.max
import kotlin.math.min

class ListTest: StringSpec() {

    private val random = Random()

    init {

        "hasSubList" {
            forAll(IntListGenerator()) { (first, second) ->
                val index = if (first.isEmpty()) 0 else random.nextInt(max(first.size - 1, 1))
                val result = second.splitAt(index)
                second.hasSubList(result.first) &&
                        second.hasSubList(result.second) &&
                        second.hasSubList(result.second.drop(min(result.second.length(), 2))) &&
                        !second.hasSubList(result.second.drop(min(result.second.length(), 2)).cons(-1))
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
