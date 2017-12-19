package com.fpinkotlin.lists.exercise15


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import com.fpinkotlin.lists.exercise15.List.Companion.flatten
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import java.util.*

class ListTest: StringSpec() {

    val f: (Int) -> (String) -> String = { a -> { b -> "$a + ($b)"} }

    init {

        "flatten0" {
            forAll(IntListListGenerator(0, 0), { list ->
                flatten(list).isEmpty()
            })
        }

        "flatten" {
            forAll(IntListListGenerator(), { list ->
                val sum1 = list.foldLeft(0) { x -> { y -> x + sum(y)} }
                val sum2 = sum(flatten(list))
                sum1 == sum2
            }, 10)
        }
    }
}

class IntListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100) : Gen<List<Int>> {

    override fun generate(): List<Int> {
        val array: Array<Int> = list(IntGenerator(100), minLength, maxLength).generate().toTypedArray()
        return List(*array)
    }
}

class IntListListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100) : Gen<List<List<Int>>> {

    override fun generate(): List<List<Int>> {
        val array: Array<List<Int>> = list(IntListGenerator(0, 10), minLength, maxLength).generate() .toTypedArray()
        return List(*array)
    }
}

class IntGenerator(private val max: Int): Gen<Int> {
    private val RANDOM = Random()
    override fun generate(): Int = RANDOM.nextInt(max)
}

