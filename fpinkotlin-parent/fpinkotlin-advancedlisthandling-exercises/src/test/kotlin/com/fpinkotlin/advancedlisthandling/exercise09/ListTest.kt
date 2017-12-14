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

    override fun generate(): Pair<List<Int>, List<Int>> {
        val array1: Array<Int> = list(Gen.int(), minLength, maxLength).generate().toTypedArray()
        val array2: Array<Int> = list(Gen.int(), minLength, maxLength).generate().toTypedArray()
        return Pair(List(*array1), List(*array2))
    }
}
