package com.fpinkotlin.advancedlisthandling.exercise24

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec
import java.util.concurrent.Executors


class ListTest: StringSpec() {

    init {

        "parMap" {
            forAll(IntListGenerator()) { (_, list) ->
                val f = { a: Int -> a * 2 }
                val es = Executors.newFixedThreadPool(4)
                list.parMap(es, f).getOrElse(Result.failure<List<Int>>("Error !")).toString() ==
                        list.map(f).toString()
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
