package com.fpinkotlin.advancedlisthandling.exercise16


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import java.util.*
import kotlin.math.max
import kotlin.math.min

class ListTest: StringSpec() {

    private val random = Random()

    init {

        "hasSubList" {
            forAll(IntListGenerator(), { (first, second) ->
                val index = if (first.isEmpty()) 0 else random.nextInt(max(first.size - 1, 1))
                val result = second.splitAt(index)
                second.hasSubList(result.first) &&
                    second.hasSubList(result.second) &&
                    second.hasSubList(result.second.drop(min(result.second.length(), 2))) &&
                    !second.hasSubList(result.second.drop(min(result.second.length(), 2)).cons(-1))
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
