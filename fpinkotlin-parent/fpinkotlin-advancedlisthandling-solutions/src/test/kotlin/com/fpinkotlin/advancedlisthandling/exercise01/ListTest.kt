package com.fpinkotlin.advancedlisthandling.exercise01


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec


class ListTest: StringSpec() {

    init {

        "length" {
            forAll(Gen.list(Gen.choose(1, 1_000))) { list ->
                List(*(list.toTypedArray())).lengthMemoized() == list.size
            }
        }
    }
}

fun main(args: Array<String>) {
    val testLimit = 35000
    val array = Array(testLimit) {i -> i.toLong() }
    val testList: List<Long> = List(*array)
    val start = System.currentTimeMillis()
    testList.map { it * 2 }
    println(System.currentTimeMillis() - start)
}