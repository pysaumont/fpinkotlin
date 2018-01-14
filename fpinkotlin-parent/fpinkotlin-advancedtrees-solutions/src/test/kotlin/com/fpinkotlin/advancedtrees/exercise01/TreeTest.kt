package com.fpinkotlin.advancedtrees.exercise01

import com.fpinkotlin.common.List
import com.fpinkotlin.common.range
import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.IntListGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    init {

        "Plus Ordered Ascending" {
            forAll(IntGenerator(0, 10_000), { i ->
                val list: List<Int> = range(1, i + 1)
                val tree: Tree<Int> = list.foldLeft(Tree()) { t -> { i -> t.plus(i) } }
                tree.size() == i && tree.height() <= (2 * log2nlz(tree.size() + 1))
            })
        }

        "Plus Ordered Descending" {
            forAll(IntGenerator(0, 10_000), { i ->
                val list: List<Int> = range(1, i + 1).reverse()
                val tree: Tree<Int> = list.foldLeft(Tree()) { t -> { i -> t.plus(i) } }
                tree.size() == i && tree.height() <= (2 * log2nlz(tree.size() + 1))
            })
        }

        "Plus Random" {
            forAll(IntListGenerator(), { (array, list) ->
                val tree: Tree<Int> = list.foldLeft(Tree()) { t -> { i -> t.plus(i) } }
                tree.size() == array.size && tree.height() <= (2 * log2nlz(tree.size() + 1))
            })
        }
    }

}