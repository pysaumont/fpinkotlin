package com.fpinkotlin.lists.exercise18


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "map" {
            forAll(IntListGenerator(), { pair ->
                sum(pair.second.map { it * 3 }) == sum(pair.second) * 3
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

fun main(args: Array<String>) {
    val testLimit = 35000
    val array = Array(testLimit) {i -> i.toLong() }
    val testList: List<Long> = List(*array)
    val start = System.currentTimeMillis()
    testList.map { it * 2 }
    println(System.currentTimeMillis() - start)
}