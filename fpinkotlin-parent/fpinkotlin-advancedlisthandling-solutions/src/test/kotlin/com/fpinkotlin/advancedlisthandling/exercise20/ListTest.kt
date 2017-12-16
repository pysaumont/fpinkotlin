package com.fpinkotlin.advancedlisthandling.exercise20


import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "exists" {
            forAll(IntListGenerator(), { (first, second) ->
                val x = IntGenerator(0, 100).generate()
                second.exists { it == x } == first.contains(x)
            })
        }
    }
}

class IntListGenerator(private val minLength: Int = 0, private val maxLength: Int = 1000) : Gen<Pair<Array<Int>, List<Int>>> {

    override fun generate(): Pair<Array<Int>, List<Int>> {
        val array: Array<Int> = list(IntGenerator(0, 100), minLength, maxLength).generate().toTypedArray()
        return Pair(array, List(*array))
    }
}
