package com.fpinkotlin.lists.exercise04


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "dropWhile" {
            forAll(IntListGenerator(), { (first, second) ->
                if (first.isEmpty())
                    second.dropWhile{it > 0}.isEmpty()
                else
                    second.dropWhile{it > 0}.toString() ==
                        first.dropWhile { it > 0 }.let { if (it.isEmpty()) "[NIL]" else it.joinToString(", ", "[", ", NIL]") }
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
