package com.fpinkotlin.advancedlisthandling.exercise12


import com.fpinkotlin.common.Result
import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import java.util.*
import kotlin.math.max

class ListTest: StringSpec() {

    private val random = Random()

    init {

        "getAt" {
            forAll(IntListGenerator(), { (first, second) ->
                val index = if (first.isEmpty()) 0 else random.nextInt(max(first.size - 1, 1))
                val error = Result.failure<Int>("Index out of bound").toString()
                second.getAt(- (index + 1)).toString() ==  error &&
                    second.getAt(first.size + index).toString() == error &&
                    second.getAt(index).toString() == if (first.isEmpty()) error else Result(first[index])
                        .toString()
            })
        }

        "getAtViaFoldLeft" {
            forAll(IntListGenerator(), { (first, second) ->
                val index = if (first.isEmpty()) 0 else random.nextInt(max(first.size - 1, 1))
                val error = Result.failure<Int>("Index out of bound").toString()
                second.getAtViaFoldLeft(- (index + 1)).toString() ==  error &&
                    second.getAtViaFoldLeft(first.size + index).toString() == error &&
                    second.getAtViaFoldLeft(index).toString() == if (first.isEmpty()) error else Result(first[index])
                        .toString()
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
