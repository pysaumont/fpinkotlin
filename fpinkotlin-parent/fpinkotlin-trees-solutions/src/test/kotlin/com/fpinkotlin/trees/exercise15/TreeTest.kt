package com.fpinkotlin.trees.exercise15


import com.fpinkotlin.common.range
import com.fpinkotlin.trees.exercise15.Tree.Companion.balance
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {


    init {
        "testPlus" {
            val limit = 100_000
            val orderedTestList = range(0, limit)
            val temp = orderedTestList.foldLeft(Tree<Int>()) { t -> { i -> t + i } }
            val tree = balance(temp)
            tree.size shouldBe limit
            (tree.height <= 2 * log2nlz(tree.size + 1)) shouldBe true
        }
    }
}
