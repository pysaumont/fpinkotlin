package com.fpinkotlin.advancedlisthandling.exercise23


import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import java.util.concurrent.Executors

class ListTest: StringSpec() {

    init {

        "parFoldLeft" {
            forAll(IntListGenerator(), { (array, list) ->
                val f = { a: Int -> { b: Int -> a + b } }
                val es = Executors.newFixedThreadPool(4)
                list.parFoldLeft(es, 0, f, f).getOrElse(0) ==  array.sum()
            })
        }
    }
}


class IntListGenerator(private val minLength: Int = 0, private val maxLength: Int = 1_000) : Gen<Pair<Array<Int>, List<Int>>> {

    override fun generate(): Pair<Array<Int>, List<Int>> {
        val array: Array<Int> = list(IntGenerator(0, 10_000), minLength, maxLength).generate().toTypedArray()
        return Pair(array, List(*array))
    }
}

