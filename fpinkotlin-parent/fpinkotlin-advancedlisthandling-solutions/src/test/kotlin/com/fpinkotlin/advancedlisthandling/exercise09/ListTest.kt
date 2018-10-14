package com.fpinkotlin.advancedlisthandling.exercise09


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "product" {
            forAll(IntListPairGenerator(0, 10), { (list1, list2) ->
                val result = product(list1, list2) { a -> { b: Int -> Pair(a, b) } }
                result.length() == list1.length() * list2.length()
            })
        }
    }
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

class IntListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100): Gen<Pair<Array<Int>, List<Int>>> {
    override fun constants(): Iterable<Pair<Array<Int>, List<Int>>> =
            listOf(Pair(arrayOf(), List()))

    override fun random(): Sequence<Pair<Array<Int>, List<Int>>> =
            list(Gen.int(), minLength, maxLength)
                    .map { Pair(it.toTypedArray(),
                            it.fold(List<Int>()) { list, i ->
                                List.cons(i, list)}) }.random()
}
