package com.fpinkotlin.lists.exercise03

import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

  init {

    "drop" {
        forAll(IntListGenerator(), { (first, second) ->
            if (first.isEmpty())
                second.isEmpty()
            else {
                (Gen.positiveIntegers().random().first() % first.size).let {
                    second.drop(it).toString() ==
                        first.drop(it).joinToString(", ", "[", ", NIL]")
                }
            }
        })
        forAll(IntListGenerator(0, 0), { (first, second) ->
            if (first.isEmpty())
                second.isEmpty()
            else {
                (Gen.positiveIntegers().random().first() % first.size).let {
                    second.drop(it).toString() ==
                        first.drop(it).joinToString(", ", "[", ", NIL]")
                }
            }
        }, 1)
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
