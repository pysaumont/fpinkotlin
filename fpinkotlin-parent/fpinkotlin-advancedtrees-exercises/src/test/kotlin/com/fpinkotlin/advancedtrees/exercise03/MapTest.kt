package com.fpinkotlin.advancedtrees.exercise03


import com.fpinkotlin.common.*
import com.fpinkotlin.common.List
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class MapTest: StringSpec() {

    init {

        "testFold" {
            val limit = 50
            val list = range(1, limit + 1)
            val expected = list.reverse().map(NumbersToEnglish.convertUS)
            val map = list.foldLeft(Map<Int, String>()) { m ->
                { m + Pair(it, NumbersToEnglish.convertUS(it)) }
            }
            val rl = sequence(map.foldLeft(List<Result<String>>(), { lst -> { me -> lst.cons(me.value) } }) { l1 ->
                { l2 ->
                    List.concat(l1, l2)
                }
            })
            rl.map {
                it.toString() == expected.toString()
            }.getOrElse(false) shouldBe true
        }

        "testValues" {
            val limit = 50
            val list = range(1, limit + 1)
            val expected = list.map(NumbersToEnglish.convertUS)
            val map = list.foldRight(Map<Int, String>()) { v ->
                { it + Pair(v, NumbersToEnglish.convertUS(v)) }
            }
            val values = map.values()
            values.toString() shouldBe expected.toString()
        }
    }
}