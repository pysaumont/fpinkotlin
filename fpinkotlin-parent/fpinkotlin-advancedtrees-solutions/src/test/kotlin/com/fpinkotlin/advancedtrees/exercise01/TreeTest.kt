package com.fpinkotlin.advancedtrees.exercise01

import com.fpinkotlin.advancedtrees.common.Option
import com.fpinkotlin.advancedtrees.common.unfold
import com.fpinkotlin.common.List
import com.fpinkotlin.common.range
import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.IntListGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {
    private val testLimit = 100_000
    private val timeFactor = 500

    init {

        "Plus Ordered Ascending" {
            forAll(IntGenerator(0, 10_000), { i ->
                val list: List<Int> = range(1, i + 1)
                val tree: Tree<Int> = list.foldLeft(Tree()) { t -> { i -> t.plus(i) } }
                tree.size == i && tree.height <= (2 * log2nlz(tree.size + 1))
            })
        }

        "Plus Ordered Descending" {
            forAll(IntGenerator(0, 10_000), { i ->
                val list: List<Int> = range(1, i + 1).reverse()
                val tree: Tree<Int> = list.foldLeft(Tree()) { t -> { i -> t.plus(i) } }
                tree.size == i && tree.height <= (2 * log2nlz(tree.size + 1))
            })
        }

        "Plus Random" {
            forAll(IntListGenerator(), { (array, list) ->
                val tree: Tree<Int> = list.foldLeft(Tree()) { t -> { i -> t.plus(i) } }
                tree.size == array.size && tree.height <= (2 * log2nlz(tree.size + 1))
            })
        }

        "testAddOrderedAscending" {
            val maxTime = 2L * log2nlz(testLimit + 1) * timeFactor
            val list = com.fpinkotlin.advancedtrees.common.range(1, testLimit + 1)
            val time = System.currentTimeMillis()
            val tree = list.foldLeft(com.fpinkotlin.advancedtrees.exercise02.Tree<Int>()) { t -> { t + it } }
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
            val tree = list.foldLeft(com.fpinkotlin.advancedtrees.exercise02.Tree<Int>()) { t -> { t + it } }
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
            forAll(IntListGenerator(0, testLimit, 0, 10_000), { (_, list) ->
                val maxTime = 2L * log2nlz(list.length + 1) * timeFactor
                val set = list.foldLeft(setOf<Int>()) { s -> { s + it }}
                val time = System.currentTimeMillis()
                val tree = list.foldLeft(com.fpinkotlin.advancedtrees.exercise02.Tree<Int>()) { t -> { t + it } }
                val duration = System.currentTimeMillis() - time
                val time2 = System.currentTimeMillis()
                val tree2 = com.fpinkotlin.advancedtrees.common.range(0, 5000).foldLeft(tree) { t -> { t - it } }
                val duration2 = System.currentTimeMillis() - time2
                (duration < maxTime) &&
                        tree.size == set.size &&
                        tree.height <= 2 * log2nlz(tree.size + 1) &&
                        duration2 < maxTime &&
                        tree2.height <= 2 * log2nlz(tree2.size + 1)
            }, 1)
        }
    }

}

fun log2nlz(n: Int): Int = when (n) {
    0 -> 0
    else -> 31 - Integer.numberOfLeadingZeros(n)
}
