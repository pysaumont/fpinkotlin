package com.fpinkotlin.advancedlisthandling.exercise08


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "zipWith" {
            forAll(IntListPairGenerator(), { (list1, list2) ->
                val result = zipWith(list1, list2) { a -> { b: Int -> Pair(a, b) } }
                result.map { it.first }.toString() == list1.reverse().drop(list1.length() - result.length()).reverse().toString() &&
                    result.map { it.second }.toString() == list2.reverse().drop(list2.length() - result.length()).reverse().toString()
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
