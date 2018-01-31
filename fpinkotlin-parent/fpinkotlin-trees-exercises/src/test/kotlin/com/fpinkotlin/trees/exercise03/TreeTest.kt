package com.fpinkotlin.trees.exercise03

import com.fpinkotlin.common.List
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    init {

        "contains" {
            val tree = Tree<Int>() + 4 + 2 + 6 + 1 + 3 + 7 + 5
            tree.contains(3) shouldBe true
        }

        "contains not" {
            val list: List<Int> = List()
            val tree = Tree(list)
            val result = tree.contains(8)
            result shouldBe false
        }

        "empty contains not" {
            val tree = Tree(List<Int>())
            tree.contains(3) shouldBe false
        }
    }

}

