package com.fpinkotlin.lists.exercise09


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import java.util.*

class ListTest: StringSpec() {

    init {

        "foldLeft" {
            forAll(IntListGenerator(), { (_, second) ->
                second.foldLeft(0) { a -> { b -> a + b } } ==  second.foldRight(0) { a -> { b -> a + b } }
            })
        }
    }
}


class IntListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100) : Gen<Pair<Array<Int>, List<Int>>> {

    override fun generate(): Pair<Array<Int>, List<Int>> {
        val array: Array<Int> = list(IntGenerator(1_00), minLength, maxLength).generate().toTypedArray()
        return Pair(array, List(*array))
    }
}

class IntGenerator(private val max: Int): Gen<Int> {
    private val RANDOM = Random()
    override fun generate(): Int = RANDOM.nextInt(max)
}

