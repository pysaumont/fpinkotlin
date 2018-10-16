package com.fpinkotlin.trees.exercise04

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    init {

        "size0" {
            val tree = Tree<Int>()
            tree.size() shouldBe 0
        }

        "size1" {
            val tree = Tree<Int>() + 4 + 2 + 6 + 1 + 3 + 7 + 5
            tree.size() shouldBe 7
        }

        "size2" {
            val tree = Tree<Int>() + 1 + 2 + 3 + 4 + 5 + 6 + 7
            tree.size() shouldBe 7
        }

        "height0" {
            val tree = Tree<Int>()
            tree.height() shouldBe -1
        }

        "height1" {
            val tree = Tree<Int>() + 4 + 2 + 6 + 1 + 3 + 7 + 5
            tree.height() shouldBe 2
        }

        "height2" {
            val tree = Tree<Int>() + 1 + 2 + 3 + 4 + 5 + 6 + 7
            tree.height() shouldBe 6
        }
    }

}
