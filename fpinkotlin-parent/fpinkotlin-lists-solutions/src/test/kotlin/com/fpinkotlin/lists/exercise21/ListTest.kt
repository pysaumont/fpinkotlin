package com.fpinkotlin.lists.exercise21


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import java.util.*

class ListTest: StringSpec() {

    init {

        "filter" {
            forAll(IntListGenerator(), { (array, list) ->
                sum(list.filter { it % 2 == 0 }) == array.fold(0) { a, b -> a + if (b % 2 == 0) b else 0 }
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
    private val random = Random()
    override fun generate(): Int = random.nextInt(max)
}
