package com.fpinkotlin.advancedtrees.exercise08


import com.fpinkotlin.common.List
import com.fpinkotlin.common.range
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class MapTest: StringSpec() {

    init {

        "pop" {
            val list = range(0, 9)
            val queue: Heap<Int> = list.foldLeft(Heap()) { h -> { h + it } }
            queue.toList().toString() shouldBe list.toString()
        }

        "pop2" {
            val list = List(7, 3, 1, 6, 4, 2, 0, 5)
            val queue: Heap<Int> = list.foldLeft(Heap()) { h -> { h + it } }
            val list2 = range (0, 8)
            queue.toList().toString() shouldBe list2.toString()
        }
    }

//    private fun toList(queue: Heap<Int>): List<Int> =
//        range(0, queue.size).foldRight(Pair(List(), queue)) { i ->
//           { plq: Pair<List<Int>, Heap<Int>> ->
//               plq.second.pop().getOrElse(Pair(i, plq.second)).let { Pair(plq.first + it.first, it.second) }
//           }
//        }.first.reverse()
}
