package com.fpinkotlin.advancedtrees.exercise02

import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    init {

        "remove lower" {
            val tree = Tree<Int>() + 2 + 1 + 3
            println(tree)
//            tree.size() shouldBe 7
//            (tree.height() <= (2 * log2nlz(tree.size() + 1))) shouldBe true
            println(tree - 1)
            println(tree - 1 - 2)
            println(tree - 1 - 2 - 3)
            println(tree - 3)
            println(tree - 3- 2)
            println(tree - 3 - 2 - 1)
        }

//        "remove equals" {
//            val tree = Tree<Int>() + 4 + 2 + 1 + 3 + 6 + 5 + 7
//            tree.remove(4).toString() shouldBe "(T (T E 1 E) 2 (T E 3 (T (T E 5 E) 6 (T E 7 E))))"
//        }
//
//        "remove higher" {
//            val tree = Tree<Int>() + 4 + 2 + 1 + 3 + 6 + 5 + 7
//            tree.remove(6).toString() shouldBe "(T (T (T E 1 E) 2 (T E 3 E)) 4 (T E 5 (T E 7 E)))"
//        }
//
//        "remove empty" {
//            val tree = Tree<Int>()
//            tree.remove(6).toString() shouldBe "E"
//        }
    }

}