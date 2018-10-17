package com.fpinkotlin.advancedlisthandling.exercise23


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec
import java.util.concurrent.Executors

class ListTest: StringSpec() {

    init {

        "parFoldLeft" {
            forAll(IntListGenerator()) { (array, list) ->
                val f = { a: Int -> { b: Int -> a + b } }
                val es = Executors.newFixedThreadPool(4)
                list.parFoldLeft(es, 0, f, f).getOrElse(0) ==  array.sum()
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
