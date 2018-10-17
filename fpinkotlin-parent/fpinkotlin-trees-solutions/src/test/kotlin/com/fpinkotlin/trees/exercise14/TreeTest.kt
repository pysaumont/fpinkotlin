package com.fpinkotlin.trees.exercise14


import com.fpinkotlin.common.range
import io.kotlintest.properties.forAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    init {

        "balanceRandom" {
            forAll { list:List<Int> ->
                val tree = list.fold(Tree<Int>()) { tree , elem ->  tree + elem }
                val result = Tree.balance(tree)
                result.size == tree.size && result.height <= log2nlz(result.size)
            }
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
