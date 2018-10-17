package com.fpinkotlin.advancedlisthandling.exercise12


import com.fpinkotlin.common.Result
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec
import java.util.*
import kotlin.math.max

class ListTest: StringSpec() {

    private val random = Random()

    init {

        "getAt" {
            forAll(IntListGenerator()) { (first, second) ->
                val index = if (first.isEmpty()) 0 else random.nextInt(max(first.size - 1, 1))
                val error = Result.failure<Int>("Index out of bound").toString()
                second.getAt(- (index + 1)).toString() ==  error &&
                        second.getAt(first.size + index).toString() == error &&
                        second.getAt(index).toString() == if (first.isEmpty()) error else Result(first[index]).toString()
            }
        }

        "getAtViaFoldLeft" {
            forAll(IntListGenerator()) { (first, second) ->
                val index = if (first.isEmpty()) 0 else random.nextInt(max(first.size - 1, 1))
                val error = Result.failure<Int>("Index out of bound").toString()
                second.getAtViaFoldLeft(- (index + 1)).toString() ==  error &&
                        second.getAtViaFoldLeft(first.size + index).toString() == error &&
                        second.getAtViaFoldLeft(index).toString() == if (first.isEmpty()) error else Result(first[index]).toString()
            }
        }
    }
}

class IntListGenerator(private val min: Int = Int.MIN_VALUE, private val max: Int = Int.MAX_VALUE): Gen<Pair<Array<Int>, List<Int>>> {

    override fun constants(): Iterable<Pair<Array<Int>, List<Int>>> =
            Gen.list(Gen.choose(min, max)).constants().map { it.toTypedArray().let { Pair(it, List(*(it))) } }

    override fun random(): Sequence<Pair<Array<Int>, List<Int>>> =
            Gen.list(Gen.choose(min, max)).random().map { it.toTypedArray().let { Pair(it, List(*(it))) } }
}
