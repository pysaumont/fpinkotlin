package com.fpinkotlin.advancedtrees.exercise02

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.common.getOrElse
import com.fpinkotlin.common.range
import io.kotlintest.specs.StringSpec
import java.util.*

class MapTest3: StringSpec() {

    private val testLimit = 100_000
    private val timeFactor = 500

    init {

        "testAddRemoveRandom" {
//            forAll(IntListGenerator(0, testLimit, 0, 10_000), { (_, list) ->
            val rnd = Random(0)
            val list = range(1, testLimit).map { rnd.nextInt(10_000) }
            val maxTime = 2L * log2nlz(list.length + 1) * timeFactor
                val set = list.foldLeft(setOf<Int>()) { s -> { s + it }}
                val time = System.currentTimeMillis()
                val map = list.foldLeft(Map3<Int, String>()) { m -> { m + Pair(it, NumbersToEnglish.convertUS(it)) } }
                val duration = System.currentTimeMillis() - time
                val time2 = System.currentTimeMillis()
                val map2 = set.fold(map) { m, i -> m - i }
                val duration2 = System.currentTimeMillis() - time2
                val result: List<Result<Boolean>> = list.map { i ->
                    map.get(i).flatMap { x -> x.value }
                        .map { y ->
                            y == NumbersToEnglish.convertUS(i) } }
                (duration < maxTime) &&
                    result.forAll { it.map { true }.getOrElse(false) } &&
                    duration2 < maxTime &&
                    map2.isEmpty()
//            }, 1)
        }
    }

    fun log2nlz(n: Int): Int = when (n) {
        0 -> 0
        else -> 31 - Integer.numberOfLeadingZeros(n)
    }
}
