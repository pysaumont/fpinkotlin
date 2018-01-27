package com.fpinkotlin.trees.exercise04

import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    init {

        "size0" {
            val tree = Tree<Int>()
            TODO("Uncomment one of the following line, corresponding to your solution")
//            tree.size shouldBe 0
//            tree.size() shouldBe 0
        }

        "size1" {
            val tree = Tree<Int>() + 4 + 2 + 6 + 1 + 3 + 7 + 5
            TODO("Uncomment one of the following line, corresponding to your solution")
//            tree.size shouldBe 7
//            tree.size() shouldBe 7
        }

        "size2" {
            val tree = Tree<Int>() + 1 + 2 + 3 + 4 + 5 + 6 + 7
            TODO("Uncomment one of the following line, corresponding to your solution")
//            tree.size shouldBe 7
//            tree.size() shouldBe 7
        }

        "height0" {
            val tree = Tree<Int>()
            TODO("Uncomment one of the following line, corresponding to your solution")
//            tree.height shouldBe -1
//            tree.height() shouldBe -1
        }

        "height1" {
            val tree = Tree<Int>() + 4 + 2 + 6 + 1 + 3 + 7 + 5
            TODO("Uncomment one of the following line, corresponding to your solution")
//            tree.height shouldBe 2
//            tree.height() shouldBe 2
        }

        "height2" {
            val tree = Tree<Int>() + 1 + 2 + 3 + 4 + 5 + 6 + 7
            TODO("Uncomment one of the following line, corresponding to your solution")
//            tree.height shouldBe 6
//            tree.height() shouldBe 6
        }
    }

}
