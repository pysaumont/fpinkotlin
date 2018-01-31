package com.fpinkotlin.advancedtrees.exercise02

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.common.getOrElse
import com.fpinkotlin.common.range
import io.kotlintest.specs.StringSpec
import java.util.*

class MapTest_: StringSpec() {

    private val testLimit = 10_000
    private val timeFactor = 500

    init {

        "testAddRemoveRandom" {
//            forAll(IntListGenerator(0, testLimit, 0, 10_000), { (_, list) ->
//            println("starting...")
            val rnd = Random(0)
//            println(rnd.nextInt(10_000))
            val list = range(1, testLimit).map { rnd.nextInt(10_000) }
//            println(list.getAt(0))
//            println(list.getAt(1))
//            println(list.getAt(2))
//            println(list.getAt(3))
//            println(list)
            val maxTime = 2L * log2nlz(list.length + 1) * timeFactor
                val set = list.foldLeft(setOf<Int>()) { s -> { s + it }}
                val time = System.currentTimeMillis()
                val map = list.foldLeft(Map_<Int, String>()) { m -> { m + Pair(it, NumbersToEnglish.convertUS(it)) } }
//            println(map)
                val duration = System.currentTimeMillis() - time
                val time2 = System.currentTimeMillis()
//                val map2 = set.fold(map) { m, i -> m - i }
                val duration2 = System.currentTimeMillis() - time2
                var count = 0
                val map2 = set.fold(map) { m, i ->
                    if (count == 5749) {
                        println()
                    }
                    println("$count: $i")
                    count++
                    m - i }
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