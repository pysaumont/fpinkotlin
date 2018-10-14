package com.fpinkotlin.lists.exercise15


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import com.fpinkotlin.lists.exercise15.List.Companion.flatten
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    val f: (Int) -> (String) -> String = { a -> { b -> "$a + ($b)"} }

    init {

        "flatten0" {
            forAll(IntListListGenerator(0, 0), { list ->
                flatten(list).isEmpty()
            })
        }

        "flatten" {
            forAll(IntListListGenerator(), { list ->
                val sum1 = list.foldLeft("") { x -> { y -> x + y.foldLeft("") { a -> { b -> a + b } } } }
                val sum2 = flatten(list).foldLeft("") { a -> { b -> a + b } }
                sum1 == sum2
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

class IntListListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100) : Gen<List<List<Int>>> {

    override fun constants(): Iterable<List<List<Int>>> = listOf()

    override fun random(): Sequence<List<List<Int>>> =
        list(IntListGenerator(0, 10), minLength, maxLength).map { it.map { it.second } }.random().map {
            it.fold(List<List<Int>>()) { list, i -> list.cons(i)}
        }
}


