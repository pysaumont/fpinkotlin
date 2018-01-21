package com.fpinkotlin.advancedlisthandling.exercise01


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "length" {
            forAll(IntListGenerator(), { (first, second) ->
                second.lengthMemoized() == first.size
            })
        }
    }
}

class IntListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100): Gen<Pair<Array<Int>, List<Int>>> {

    override fun generate(): Pair<Array<Int>, List<Int>> {
        val array: Array<Int> = list(Gen.int(), minLength, maxLength).generate().toTypedArray()
        return Pair(array, List(*array))
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