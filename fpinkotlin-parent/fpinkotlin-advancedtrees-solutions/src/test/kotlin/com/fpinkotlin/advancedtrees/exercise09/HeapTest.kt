package com.fpinkotlin.advancedtrees.exercise09


import com.fpinkotlin.advancedtrees.common.List
import com.fpinkotlin.advancedtrees.common.range
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class HeapTest: StringSpec() {

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
