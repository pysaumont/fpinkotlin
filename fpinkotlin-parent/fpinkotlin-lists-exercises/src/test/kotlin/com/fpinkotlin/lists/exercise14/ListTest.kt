package com.fpinkotlin.lists.exercise14


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "concat0" {
            forAll(IntListGenerator(0, 0), { (_, second) ->
                sum(second) == sum(second.concat(List())) &&
                    sum(second) == sum(List<Int>().concat(second))
            })
        }

        "concat" {
            forAll(IntListPairGenerator(), { (list1, list2) ->
                val concat1 = list1.concat(list2).drop(1)
                val concat2 = list2.reverse().concat(list1.reverse()).reverse().drop(1)
                sum(concat1) == sum(concat2)
            }, 10)
        }

        "concat_" {
            forAll(IntListPairGenerator(), { (list1, list2) ->
                val concat1 = list1.concatViaFoldRight(list2).drop(1)
                val concat2 = list2.reverse().concatViaFoldRight(list1.reverse()).reverse().drop(1)
                sum(concat1) == sum(concat2)
            }, 10)
        }
    }
}

class IntListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100): Gen<Pair<Array<Int>, List<Int>>> {

    override fun constants(): Iterable<Pair<Array<Int>, List<Int>>> = listOf(Pair(arrayOf(), List()))

    override fun random(): Sequence<Pair<Array<Int>, List<Int>>> =
            list(Gen.int(), minLength, maxLength)
                    .map { Pair(it.toTypedArray(),
                            it.fold(List<Int>()) { list, i ->
                                list.cons(i) }) }.random()
}

class IntListPairGenerator(private val minLength: Int = 0, private val maxLength: Int = 100) : Gen<Pair<List<Int>, List<Int>>> {

    override fun constants(): Iterable<Pair<List<Int>, List<Int>>> =
            listOf(Pair(List(), List()))

    override fun random(): Sequence<Pair<List<Int>, List<Int>>> =
            IntListGenerator(minLength, maxLength).random().let {
                IntListGenerator(minLength, maxLength).random().zip(it)
                        .map { Pair(it.first.second, it.second.second) }
            }
}
