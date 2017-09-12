package com.fpinkotlin.lists.exercise05


import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "tail" {
            forAll(IntListGenerator(), { (first, second) ->
                if (first.isEmpty())
                    second.isEmpty()
                else
                    second.init().toString() ==
                        first.dropLast(1).let { if (it.isEmpty()) "[NIL]" else it.joinToString(", ", "[", ", NIL]") }
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



