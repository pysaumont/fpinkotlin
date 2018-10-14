package com.fpinkotlin.trees.exercise13


import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    init {

        "testToListInOrderRight" {
            val tree2 = Tree(4, 6, 7, 5, 2, 1, 3)
            tree2.toListInOrderRight().toString() shouldBe "[7, 6, 5, 4, 3, 2, 1, NIL]"
        }
    }
}
