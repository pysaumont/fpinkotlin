package com.fpinkotlin.advancedtrees.exercise02

import com.fpinkotlin.advancedtrees.common.Option
import com.fpinkotlin.advancedtrees.common.range
import com.fpinkotlin.advancedtrees.common.unfold
import com.fpinkotlin.generators.IntListGenerator
import io.kotlintest.properties.forAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    private val testLimit = 100_000
    private val timeFactor = 500

    init {

        "testAddOrderedAscending" {
            val maxTime = 2L * log2nlz(testLimit + 1) * timeFactor
            val list = range(1, testLimit + 1)
            val time = System.currentTimeMillis()
            val tree = list.foldLeft(Tree<Int>()) { t -> { t + it } }
            val duration = System.currentTimeMillis() - time
            (duration < maxTime) shouldBe true
            tree.size shouldBe testLimit
            (tree.height <= 2 * log2nlz(tree.size + 1)) shouldBe true
            val time2 = System.currentTimeMillis()
            val tree2 = list.foldLeft(tree) { t -> { t - it } }
            val duration2 = System.currentTimeMillis() - time2
            (duration2 < maxTime) shouldBe true
            tree2.isEmpty shouldBe true
        }

        "testAddOrderedDescending" {
            val maxTime = 2L * log2nlz(testLimit + 1) * timeFactor
            val list = unfold(testLimit) { s -> if (s > 0) Option(Pair(s, s - 1)) else Option() }
            val time = System.currentTimeMillis()
            val tree = list.foldLeft(Tree<Int>()) { t -> { t + it } }
            val duration = System.currentTimeMillis() - time
            (duration < maxTime) shouldBe true
            tree.size shouldBe testLimit
            (tree.height <= 2 * log2nlz(tree.size + 1)) shouldBe true
            val time2 = System.currentTimeMillis()
            val tree2 = list.foldLeft(tree) { t -> { t - it } }
            val duration2 = System.currentTimeMillis() - time2
            (duration2 < maxTime) shouldBe true
            tree2.isEmpty shouldBe true
        }

        "testAddRemoveRandom" {
            forAll(1, IntListGenerator(0, testLimit)) { (_, list) ->
                val maxTime = 2L * log2nlz(list.length + 1) * timeFactor
                val set = list.foldLeft(setOf<Int>()) { s -> { s + it }}
                val time = System.currentTimeMillis()
                val tree = list.foldLeft(Tree<Int>()) { t -> { t + it } }
                val duration = System.currentTimeMillis() - time
                val time2 = System.currentTimeMillis()
                val tree2 = range(0, 5000).foldLeft(tree) { t -> { t - it } }
                val duration2 = System.currentTimeMillis() - time2
                (duration < maxTime) &&
                    tree.size == set.size &&
                    tree.height <= 2 * log2nlz(tree.size + 1) &&
                    duration2 < maxTime &&
                    tree2.height <= 2 * log2nlz(tree2.size + 1)
            }
        }
    }

    private fun log2nlz(n: Int): Int = when (n) {
        0 -> 0
        else -> 31 - Integer.numberOfLeadingZeros(n)
    }
}
