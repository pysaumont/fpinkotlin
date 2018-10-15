package com.fpinkotlin.advancedtrees.exercise01

import com.fpinkotlin.advancedtrees.common.List
import com.fpinkotlin.advancedtrees.common.range
import com.fpinkotlin.generators.IntListGenerator
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    var n = 0
    init {

        "Plus Ordered Ascending" {
            forAll(Gen.choose(0, 1_000)) { i ->
                val list: List<Int> = range(1, i + 1)
                val tree: Tree<Int> = list.foldLeft(Tree()) { t -> { i -> t.plus(i) } }
                tree.size == i && tree.height <= (2 * log2nlz(tree.size + 1))
            }
        }

        "Plus Ordered Descending" {
            forAll(Gen.choose(0, 1_000)) { i ->
                val list: List<Int> = range(1, i + 1).reverse()
                val tree: Tree<Int> = list.foldLeft(Tree()) { t -> { i -> t.plus(i) } }
                tree.size == i && tree.height <= (2 * log2nlz(tree.size + 1))
            }
        }

        "Plus Random" {
            forAll(1, IntListGenerator()) { (array, list) ->
                val tree: Tree<Int> = list.foldLeft(Tree()) { t -> { i -> t.plus(i) } }
                tree.size == array.size && tree.height <= (2 * log2nlz(tree.size + 1))
            }
        }
    }

}

fun log2nlz(n: Int): Int = when (n) {
    0 -> 0
    else -> 31 - Integer.numberOfLeadingZeros(n)
}