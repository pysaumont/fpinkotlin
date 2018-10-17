package com.fpinkotlin.trees.exercise12


import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    init {

        "testRotateLeft" {
            val tree = Tree(4, 6, 7, 5, 2, 1, 3)
            tree.toString() shouldBe "(T (T (T E 1 E) 2 (T E 3 E)) 4 (T (T E 5 E) 6 (T E 7 E)))"
            val tree1 = tree.rotateLeft()
            tree1.toString() shouldBe "(T (T (T (T E 1 E) 2 (T E 3 E)) 4 (T E 5 E)) 6 (T E 7 E))"
            val tree2 = tree1.rotateLeft()
            tree2.toString() shouldBe "(T (T (T (T (T E 1 E) 2 (T E 3 E)) 4 (T E 5 E)) 6 E) 7 E)"
            val tree3 = tree2.rotateLeft()
            tree3.toString() shouldBe "(T (T (T (T (T E 1 E) 2 (T E 3 E)) 4 (T E 5 E)) 6 E) 7 E)"
        }

        "testRotateRight" {
            val tree = Tree(7, 6, 5, 4, 3, 2, 1)
            val tree1 = tree.rotateRight()
            tree1.toString() shouldBe "(T (T (T (T (T (T E 1 E) 2 E) 3 E) 4 E) 5 E) 6 (T E 7 E))"
            val tree2 = tree1.rotateRight()
            tree2.toString() shouldBe "(T (T (T (T (T E 1 E) 2 E) 3 E) 4 E) 5 (T E 6 (T E 7 E)))"
            val tree3 = tree2.rotateRight()
            tree3.toString() shouldBe "(T (T (T (T E 1 E) 2 E) 3 E) 4 (T E 5 (T E 6 (T E 7 E))))"
            val tree4 = tree3.rotateRight()
            tree4.toString() shouldBe "(T (T (T E 1 E) 2 E) 3 (T E 4 (T E 5 (T E 6 (T E 7 E)))))"
            val tree5 = tree4.rotateRight()
            tree5.toString() shouldBe "(T (T E 1 E) 2 (T E 3 (T E 4 (T E 5 (T E 6 (T E 7 E))))))"
            val tree6 = tree5.rotateRight()
            tree6.toString() shouldBe "(T E 1 (T E 2 (T E 3 (T E 4 (T E 5 (T E 6 (T E 7 E)))))))"
            val tree7 = tree6.rotateRight()
            tree7.toString() shouldBe "(T E 1 (T E 2 (T E 3 (T E 4 (T E 5 (T E 6 (T E 7 E)))))))"
        }
    }
}
