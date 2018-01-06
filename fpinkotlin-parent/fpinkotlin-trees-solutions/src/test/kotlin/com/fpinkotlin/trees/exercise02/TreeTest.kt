package com.fpinkotlin.trees.exercise02

import com.fpinkotlin.common.List
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    init {

        "constructor List" {
            val tree = Tree(List(4, 2, 6, 1, 3, 7, 5))
            tree.toString() shouldBe "(T (T (T E 1 E) 2 (T E 3 E)) 4 (T (T E 5 E) 6 (T E 7 E)))"
        }

        "constructor empty List" {
            val tree = Tree(List<Int>())
            tree.toString() shouldBe "E"
        }
    }

}
