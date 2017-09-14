package com.fpinkotlin.lists.exercise11


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import java.util.*

class ListTest: StringSpec() {

    init {

        "reverse0" {
            forAll(IntListGenerator(0, 0), { (_, second) ->
                second.reverse().length() == 0
            }, 1)
        }

        "reverse" {
            forAll(IntListGenerator(), { (first, second) ->
                first.reverse()
                val tsrif = List(*first)
                val dnoces = second.reverse()
                println()
                sum(tsrif) == sum(dnoces) && sum(tsrif.drop(1)) == sum(dnoces.drop(1))
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

