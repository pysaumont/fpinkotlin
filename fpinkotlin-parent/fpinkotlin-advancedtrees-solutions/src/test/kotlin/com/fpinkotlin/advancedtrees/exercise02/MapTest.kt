package com.fpinkotlin.advancedtrees.exercise02

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.common.getOrElse
import com.fpinkotlin.generators.IntListGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class MapTest: StringSpec() {

    private val testLimit = 100_000
    private val timeFactor = 500

    init {

        "testAddRemoveRandom" {
            forAll(IntListGenerator(0, testLimit, 0, 10_000), { (_, list) ->
                val maxTime = 2L * log2nlz(list.length + 1) * timeFactor
                val set = list.foldLeft(setOf<Int>()) { s -> { s + it }}
                val time = System.currentTimeMillis()
                val map = list.foldLeft(Map<Int, String>()) { m ->
                    { m + Pair(it, NumbersToEnglish.convertUS(it)) }
                }
                val duration = System.currentTimeMillis() - time
                val time2 = System.currentTimeMillis()
                val duration2 = System.currentTimeMillis() - time2
                val map2 = set.fold(map) { m, i -> m - i }
                val result: List<Result<Boolean>> = list.map { i ->
                    map.get(i).flatMap { x -> x.value }
                        .map { y ->
                            y == NumbersToEnglish.convertUS(i) } }
                (duration < maxTime) &&
                    result.forAll { it.map { true }.getOrElse(false) } &&
                    duration2 < maxTime &&
                    map2.isEmpty()
            }, 1)
        }
    }

    private fun log2nlz(n: Int): Int = when (n) {
        0 -> 0
        else -> 31 - Integer.numberOfLeadingZeros(n)
    }
}