package com.fpinkotlin.advancedtrees.exercise08


import com.fpinkotlin.common.List
import com.fpinkotlin.common.range
import io.kotlintest.shouldBe
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
}
