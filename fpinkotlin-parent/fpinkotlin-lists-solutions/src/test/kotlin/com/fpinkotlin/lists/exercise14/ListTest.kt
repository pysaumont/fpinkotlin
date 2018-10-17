package com.fpinkotlin.lists.exercise14


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "concat" {
            forAll(IntListGenerator(), IntListGenerator()) { (_, list1), (_, list2) ->
                val concat1 = list1.concat(list2).drop(1)
                val concat2 = list2.reverse().concat(list1.reverse()).reverse().drop(1)
                sum(concat1) == sum(concat2)
            }
        }

        "concatViaFoldRight" {
            forAll(IntListGenerator(), IntListGenerator()) { (_, list1), (_, list2) ->
                val concat1 = list1.concatViaFoldRight(list2).drop(1)
                val concat2 = list2.reverse().concatViaFoldRight(list1.reverse()).reverse().drop(1)
                sum(concat1) == sum(concat2)
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
