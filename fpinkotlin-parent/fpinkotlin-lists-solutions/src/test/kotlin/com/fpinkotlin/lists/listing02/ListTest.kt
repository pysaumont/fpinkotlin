package com.fpinkotlin.lists.listing02


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "foldRight" {
            forAll(IntListGenerator(), { (_, second) ->
                second.foldRight(List()) { x: Int -> { y: List<Int> -> y.cons(x) } }.let {
                    sum(it) == sum(second) && sum(it.drop(1)) == sum(second.drop(1))
                }
            })
        }
    }
}

class IntListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100) : Gen<Pair<Array<Int>, List<Int>>> {

    override fun generate(): Pair<Array<Int>, List<Int>> {
        val array: Array<Int> = list(Gen.int(), minLength, maxLength).generate().toTypedArray()
        return Pair(array, List(*array))
    }
}
