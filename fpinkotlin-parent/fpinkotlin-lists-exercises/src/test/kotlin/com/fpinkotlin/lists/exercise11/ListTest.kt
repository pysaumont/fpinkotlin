package com.fpinkotlin.lists.exercise11


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "reverse0" {
            forAll(IntListGenerator(0, 0), { (_, second) ->
                second.reverse().length() == 0
            }, 1)
        }

        "reverse" {
            forAll(IntListGenerator(), { (first, second) ->
                first.reverse()
                val tsrif = List(*first)
                val dnoces = second.reverse()
                println()
                sum(tsrif) == sum(dnoces) && sum(tsrif.drop(1)) == sum(dnoces.drop(1))
            })
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
