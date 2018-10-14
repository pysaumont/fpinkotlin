package com.fpinkotlin.trees.exercise11


import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    init {

        "testMap" {
            val tree = Tree(4, 2, 1, 3, 6, 5, 7)
            val result = tree.map { x -> x + 2 }
            result.toString() shouldBe "(T (T (T E 3 E) 4 (T E 5 E)) 6 (T (T E 7 E) 8 (T E 9 E)))"
        }

        "testMap2" {
            val tree = Tree(-4, 2, -1, 3, 6, -5, 7)
            val result = tree.map { x -> x * x }
            result.toString() shouldBe "(T (T (T E 1 E) 4 (T E 9 E)) 16 (T E 25 (T E 36 (T E 49 E))))"
        }
    }
}
