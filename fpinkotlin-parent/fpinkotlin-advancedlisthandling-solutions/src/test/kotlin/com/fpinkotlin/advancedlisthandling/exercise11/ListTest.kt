package com.fpinkotlin.advancedlisthandling.exercise11


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "unZip" {
            forAll(IntListPairGenerator(), { (list1, list2) ->
                val zip = zipWith(list1, list2) { a -> { b: Int -> Pair(a, b) } }
                val result = zip.unzip { it }
                result.first.toString() ==
                    list1.reverse().drop(list1.length() - result.first.length()).reverse().toString() &&
                    result.second.toString() ==
                        list2.reverse().drop(list2.length() - result.second.length()).reverse().toString()
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
