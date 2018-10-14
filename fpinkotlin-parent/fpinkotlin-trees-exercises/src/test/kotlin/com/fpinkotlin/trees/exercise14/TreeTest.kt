package com.fpinkotlin.trees.exercise14


import com.fpinkotlin.common.range
import com.fpinkotlin.generators.IntListGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    init {

        "balanceRandom" {
            forAll(IntListGenerator(0, 2_000), { (_, list) ->
                val tree = list.foldLeft(Tree<Int>()) { tree -> { elem ->  tree + elem } }
                val result = Tree.balance(tree)
                result.size == tree.size && result.height == log2nlz(result.size)
            }, 100)
        }

        "balanceOrdered" {
            val orderedTestList = range(0, 2_000)
            val orderedTree = orderedTestList.foldLeft(Tree()) { t: Tree<Int> -> { i: Int -> t + i } }
            val result = Tree.balance(orderedTree)
            result.size shouldBe orderedTree.size
            result.height shouldBe log2nlz(result.size)
        }
    }
}
