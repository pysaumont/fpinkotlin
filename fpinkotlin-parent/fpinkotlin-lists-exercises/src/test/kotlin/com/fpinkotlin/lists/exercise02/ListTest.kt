package com.fpinkotlin.lists.exercise02

import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "setHead" {
            val i = Gen.int().generate()
            forAll(IntListGenerator(1, 100), { (first, second) ->
                second.setHead(i).toString() ==
                        "[$i, ${first.joinToString(", ", "", ", NIL]").substringAfter(", ")}"
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
