package com.fpinkotlin.advancedlisthandling.exercise03


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "lastSafe" {
            forAll(IntListGenerator(), { (first, second) ->
                second.lastSafe().getOrElse(0) ==  if (first.isNotEmpty()) first[first.size - 1] else 0
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
                                List.cons(i, list)}) }.random()
}
