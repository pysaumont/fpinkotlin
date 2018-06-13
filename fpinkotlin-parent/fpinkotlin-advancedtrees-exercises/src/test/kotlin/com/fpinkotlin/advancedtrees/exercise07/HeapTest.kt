package com.fpinkotlin.advancedtrees.exercise07


import com.fpinkotlin.common.List
import com.fpinkotlin.common.range
import com.fpinkotlin.common.sequence
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class HeapTest : StringSpec() {

    init {

        "plus" {
            val list = range(0, 9)
            val queue: Heap<Int> = list.foldLeft(Heap()) { h -> { h + it } }
            sequence(list.map { i ->
                queue.get(i).map { it == i }
            }).map {
                it.foldLeft(true) { a ->
                    { b -> a && b }
                }
            }.getOrElse(false) shouldBe true
        }

        "plus2" {
            val list = List(7, 3, 1, 6, 4, 2, 0, 5)
            val queue: Heap<Int> = list.foldLeft(Heap()) { h -> { h + it } }
            val list2 = range(0, 8)
            sequence(list2.map { i ->
                queue.get(i).map { it == i }
            }).map {
                it.foldLeft(true) { a ->
                    { b -> a && b }
                }
            }.getOrElse(false) shouldBe true
        }
    }
}
