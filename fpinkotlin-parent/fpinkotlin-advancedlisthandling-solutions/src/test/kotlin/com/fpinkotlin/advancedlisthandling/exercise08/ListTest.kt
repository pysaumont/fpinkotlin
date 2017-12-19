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

    override fun generate(): Pair<List<Int>, List<Int>> {
        val array1: Array<Int> = list(Gen.int(), minLength, maxLength).generate().toTypedArray()
        val array2: Array<Int> = list(Gen.int(), minLength, maxLength).generate().toTypedArray()
        return Pair(List(*array1), List(*array2))
    }
}
