package com.fpinkotlin.trees.exercise01

import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    init {

        "plus" {
            val tree = Tree<Int>() + 4 + 2 + 6 + 1 + 3 + 7 + 5
            tree.toString() shouldBe "(T (T (T E 1 E) 2 (T E 3 E)) 4 (T (T E 5 E) 6 (T E 7 E)))"
        }
    }

}
