package com.fpinkotlin.advancedlisthandling.exercise22

import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import java.util.*


class ListTest: StringSpec() {

    private val random = Random()

    init {

        "splitListAt" {
            forAll(IntListGenerator(), { (_, second) ->
                val splitPosition = if (second.length() == 0) 0 else  random.nextInt(second.length())
                val result = second.splitListAt(splitPosition)
                result.flatMap { it }.toString() == second.toString()
            })
        }

        "divide" {
            forAll(IntListGenerator(), { (_, second) ->
                val depth = random.nextInt(8)
                val result = second.divide(depth)
                result.flatMap { it }.toString() == second.toString()
            })
        }
    }
}

class IntListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100): Gen<Pair<Array<Int>, List<Int>>> {
    override fun constants(): Iterable<Pair<Array<Int>, List<Int>>> =
            listOf(Pair(arrayOf(), List()))

    override fun random(): Sequence<Pair<Array<Int>, List<Int>>> =
            list(Gen.int(), minLength, maxLength)
                    .map { Pair(it.toTypedArray(),
                            it.fold(List<Int>()) { list, i ->
                                List.cons(i, list)}) }.random()
}
