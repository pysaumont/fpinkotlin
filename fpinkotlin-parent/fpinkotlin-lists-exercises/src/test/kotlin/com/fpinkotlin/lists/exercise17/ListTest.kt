package com.fpinkotlin.lists.exercise17


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "doubleToString" {
            forAll(DoubleListGenerator(), { (array, list) ->
                doubleToString(list).toString() == if (array.isEmpty()) "[NIL]" else array.joinToString(", ", "[", ", NIL]")
            })
        }
    }
}

class DoubleListGenerator(private val minLength: Int = 0,
                          private val maxLength: Int = 100): Gen<Pair<Array<Double>, List<Double>>> {

    override fun constants(): Iterable<Pair<Array<Double>, List<Double>>> = listOf(Pair(arrayOf(), List()))

    override fun random(): Sequence<Pair<Array<Double>, List<Double>>> =
            list(Gen.double(), minLength, maxLength)
                    .map { Pair(it.toTypedArray(),
                            it.fold(List<Double>()) { list, i ->
                                List.cons(i, list)}) }.random()
}
