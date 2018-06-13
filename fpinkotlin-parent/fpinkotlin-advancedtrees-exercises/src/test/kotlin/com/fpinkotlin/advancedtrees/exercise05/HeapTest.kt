package com.fpinkotlin.advancedtrees.exercise05


import com.fpinkotlin.common.List
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class HeapTest: StringSpec() {

    init {

        "plus" {
            val list = List(1, 2, 3, 4, 5, 6, 7)
            val queue: Heap<Int> = list.foldLeft(Heap()) { h -> { h + it } }
            queue.head.map { a -> a == 1 }.getOrElse(false) shouldBe true
        }

        "plus2" {
            val list = List(7, 3, 1, 6, 4, 6, 2)
            val queue: Heap<Int> = list.foldLeft(Heap()) { h -> { h + it } }
            queue.head.map { a -> a == 1 }.getOrElse(false) shouldBe true
        }
    }
}