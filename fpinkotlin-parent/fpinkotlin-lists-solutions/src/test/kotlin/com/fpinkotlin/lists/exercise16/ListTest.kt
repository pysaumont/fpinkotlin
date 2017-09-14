package com.fpinkotlin.lists.exercise16


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import java.util.*

class ListTest: StringSpec() {

    init {

        "triple" {
            forAll(IntListGenerator(), { list ->
                sum(triple(list)) == sum(list) * 3
            })
        }
    }
}

class IntListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100) : Gen<List<Int>> {

    override fun generate(): List<Int> {
        val array: Array<Int> = list(IntGenerator(100), minLength, maxLength).generate().toTypedArray()
        return List(*array)
    }
}

class IntGenerator(private val max: Int): Gen<Int> {
    private val RANDOM = Random()
    override fun generate(): Int = RANDOM.nextInt(max)
}
