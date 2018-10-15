package com.fpinkotlin.advancedtrees.exercise02

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class MapTest: StringSpec() {

    private val timeFactor = 500

    init {

        "testAddRemoveRandom" {
            forAll( Gen.list(Gen.choose(1, 1000))) { list ->
                println(list)
                val maxTime = 2L * log2nlz(list.size + 1) * timeFactor
                val set = list.fold(setOf<Int>()) { s, t ->  s + t }
                val time = System.currentTimeMillis()
                val map = list.fold(Map<Int, String>()) { m, n ->
                    m + Pair(n, NumbersToEnglish.convertUS(n))
                }
                val duration = System.currentTimeMillis() - time
                val time2 = System.currentTimeMillis()
                val duration2 = System.currentTimeMillis() - time2
                val map2 = set.fold(map) { m, i -> m - i }
//                val result = list.map { i ->
//                    map[i].flatMap { x -> x.value }
//                        .map { y ->
//                            y == NumbersToEnglish.convertUS(i) } }
//                (list.isEmpty() || duration < maxTime) &&
//                    result.all { it.map { true }.getOrElse(false) } &&
//                    (list.isEmpty() || duration2 < maxTime) &&
//                    map2.isEmpty()
                TODO("Uncomment previous code after implementing MapEntry")
            }
        }
    }

    private fun log2nlz(n: Int): Int = when (n) {
        0 -> 0
        else -> 31 - Integer.numberOfLeadingZeros(n)
    }
}