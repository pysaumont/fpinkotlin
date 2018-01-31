package com.fpinkotlin.trees.exercise06

import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    init {

        "remove lower" {
            val tree = Tree<Int>() + 4 + 2 + 1 + 3 + 6 + 5 + 7
            tree.remove(2).toString() shouldBe "(T (T E 1 (T E 3 E)) 4 (T (T E 5 E) 6 (T E 7 E)))"
        }

        "remove equals" {
            val tree = Tree<Int>() + 4 + 2 + 1 + 3 + 6 + 5 + 7
            tree.remove(4).toString() shouldBe "(T (T E 1 E) 2 (T E 3 (T (T E 5 E) 6 (T E 7 E))))"
        }

        "remove higher" {
            val tree = Tree<Int>() + 4 + 2 + 1 + 3 + 6 + 5 + 7
            tree.remove(6).toString() shouldBe "(T (T (T E 1 E) 2 (T E 3 E)) 4 (T E 5 (T E 7 E)))"
        }

        "remove empty" {
            val tree = Tree<Int>()
            tree.remove(6).toString() shouldBe "E"
        }
    }

}