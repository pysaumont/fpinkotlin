package com.fpinkotlin.lists.exercise01

import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "cons0" {
            val i = Gen.int().generate()
            forAll(IntListGenerator(0, 0), { (first, second) ->
                second.cons(i).toString() == first.let { if (it.isEmpty()) "[$i, NIL]" else it.joinToString(", ", "[$i, ", ", NIL]") }
            }, 1)
        }

        "cons" {
            val i = Gen.int().generate()
            forAll(IntListGenerator(), { (first, second) ->
                second.cons(i).toString() == first.let { if (it.isEmpty()) "[$i, NIL]" else it.joinToString(", ", "[$i, ", ", NIL]") }
            }, 10)
        }
    }
}

class IntListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100) : Gen<Pair<Array<Int>, List<Int>>> {

    override fun generate(): Pair<Array<Int>, List<Int>> {
        val array: Array<Int> = list(Gen.int(), minLength, maxLength).generate().toTypedArray()
        return Pair(array, List(*array))
    }
}
