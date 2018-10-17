package com.fpinkotlin.trees.exercise09


import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    private val tree = Tree(4, 2, 1, 3, 6, 5, 7)

    init {

        "testFoldInOrder_inOrderLeft" {
            val f = { s1: String -> { i: Int -> { s2: String -> "$s1$i$s2" } } }
            tree.foldInOrder("", f) shouldBe "1234567"
        }

        "testFoldInOrder_preOrderLeft" {
            val f = { s1: String -> { i: Int -> { s2: String -> "$i$s1$s2" } } }
            tree.foldInOrder("", f) shouldBe "4213657"
        }

        "testFoldInOrder_postOrderLeft" {
            val f = { s1: String -> { i: Int -> { s2: String -> "$s1$s2$i" } } }
            tree.foldInOrder("", f) shouldBe "1325764"
        }

        "testFoldInOrder_inOrderRight" {
            val f = { s1: String -> { i: Int -> { s2: String -> "$s2$i$s1" } } }
            tree.foldInOrder("", f) shouldBe "7654321"
        }

        "testFoldInOrder_preOrderRight" {
            val f = { s1: String -> { i: Int -> { s2: String -> "$s2$s1$i" } } }
            tree.foldInOrder("", f) shouldBe "7563124"
        }

        "testFoldInOrder_postOrderRight" {
            val f = { s1: String -> { i: Int -> { s2: String -> "$i$s2$s1" } } }
            tree.foldInOrder("", f) shouldBe "4675231"
        }

        "testFoldPreOrder_preOrderLeft" {
            val f = { i: Int -> { s1: String -> { s2: String -> "$i$s1$s2" } } }
            tree.foldPreOrder("", f) shouldBe "4213657"
        }

        "testFoldPreOrder_inOrderLeft" {
            val f = { i: Int -> { s1: String -> { s2: String -> "$s1$i$s2" } } }
            tree.foldPreOrder("", f) shouldBe "1234567"
        }

        "testFoldPreOrder_preOrderRight" {
            val f = { i: Int -> { s1: String -> { s2: String -> "$i$s2$s1" } } }
            tree.foldPreOrder("", f) shouldBe "4675231"
        }

        "testFoldPreOrder_postOrderRight" {
            val f = { i: Int -> { s1: String -> { s2: String -> "$s2$s1$i" } } }
            tree.foldPreOrder("", f) shouldBe "7563124"
        }

        "testFoldPreOrder_inOrderRight" {
            val f = { i: Int -> { s1: String -> { s2: String -> "$s2$i$s1" } } }
            tree.foldPreOrder("", f) shouldBe "7654321"
        }

        "testFoldPreOrder_postOrderLeft" {
            val f = { i: Int -> { s1: String -> { s2: String -> "$s1$s2$i" } } }
            tree.foldPreOrder("", f) shouldBe "1325764"
        }

        "testFoldPostOrder_postOrderLeft" {
            val f = { s1: String -> { s2: String -> { i: Int -> "$s1$s2$i" } } }
            tree.foldPostOrder("", f) shouldBe "1325764"
        }

        "testFoldPostOrder_postOrderRight" {
            val f = { s1: String -> { s2: String -> { i: Int -> "$s2$s1$i" } } }
            tree.foldPostOrder("", f) shouldBe "7563124"
        }

        "testFoldPostOrder_inOrderLeft" {
            val f = { s1: String -> { s2: String -> { i: Int -> "$s1$i$s2" } } }
            tree.foldPostOrder("", f) shouldBe "1234567"
        }

        "testFoldPostOrder_inOrderRight" {
            val f = { s1: String -> { s2: String -> { i: Int -> "$i$s2$s1" } } }
            tree.foldPostOrder("", f) shouldBe "4675231"
        }

        "testFoldPostOrder_preOrderRight" {
            val f = { s1: String -> { s2: String -> { i: Int -> "$i$s1$s2" } } }
            tree.foldPostOrder("", f) shouldBe "4213657"
        }

        "testFoldPostOrder_preOrderLeft" {
            val f = { s1: String -> { s2: String -> { i: Int -> "$s2$i$s1" } } }
            tree.foldPostOrder("", f) shouldBe "7654321"
        }
    }
}