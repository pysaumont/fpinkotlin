package com.fpinkotlin.trees.exercise10


import com.fpinkotlin.common.List
import com.fpinkotlin.common.Option
import com.fpinkotlin.common.unfold
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    init {

        "testListFoldRight" {
            val list1: List<Int> = unfold(0) { i ->
                if (i < 20)
                    Option(Pair(i, i + 1))
                else
                    Option()
            }
            val list2 = list1.foldRight(List<Int>()) { i -> { l -> l.cons(i) } }
            list1.toString() shouldBe list2.toString()
        }

        "testListFoldLeft" {
            val list1: List<Int> = unfold(0) { i ->
                if (i < 20)
                    Option(Pair(i, i + 1))
                else
                    Option()
            }
            val list2 = list1.foldLeft(List<Int>()) { l -> { i -> l.reverse().cons(i).reverse() } }
            list1.toString() shouldBe list2.toString()
        }

        "testMergeLeftEmpty" {
            val tree = Tree(6, 7, 5, 9, 8)
            val result = Tree(Tree(), 4, tree)
            result.toString() shouldBe "(T E 4 (T (T E 5 E) 6 (T E 7 (T (T E 8 E) 9 E))))"
        }

        "testMergeLeftEmptyNok" {
            val tree = Tree(4, 6, 7, 5, 2, 1, 0)
            val result = Tree(Tree(), 4, tree)
            result.toString() shouldBe "(T (T (T (T E 0 E) 1 E) 2 E) 4 (T (T E 5 E) 6 (T E 7 E)))"
        }

        "testMergeRightEmpty" {
            val tree = Tree(4, 6, 7, 5, 2, 1, 0)
            val result = Tree(tree, 4, Tree())
            result.toString() shouldBe "(T (T (T (T E 0 E) 1 E) 2 E) 4 (T (T E 5 E) 6 (T E 7 E)))"
        }

        "testMergeNok" {
            val tree1 = Tree(4, 6, 7, 5, 2)
            val tree2 = Tree(7, 5, 2, 1, 0)
            val result = Tree(tree1, 4, tree2)
            result.toString() shouldBe "(T (T (T (T E 0 E) 1 E) 2 E) 4 (T (T E 5 E) 6 (T E 7 E)))"
        }

        "testMergeEmpty" {
            val result = Tree(Tree(), 4, Tree())
            result.toString() shouldBe "(T E 4 E)"
        }

        "testMerge" {
            val tree1 = Tree(2, 1, 3)
            val tree2 = Tree(6, 5, 7)
            val result = Tree(tree1, 4, tree2)
            result.toString() shouldBe "(T (T (T E 1 E) 2 (T E 3 E)) 4 (T (T E 5 E) 6 (T E 7 E)))"
            result.foldPreOrder(List<Int>()) { i -> { l1 -> { l2 -> List(i).concat(l1).concat(l2) } } }.toString() shouldBe "[4, 2, 1, 3, 6, 5, 7, NIL]"
        }

        "testMergeReverseOrder" {
            val tree1 = Tree(2, 1, 3)
            val tree2 = Tree(6, 5, 7)
            val result = Tree(tree2, 4, tree1)
            result.toString() shouldBe "(T (T (T E 1 E) 2 (T E 3 E)) 4 (T (T E 5 E) 6 (T E 7 E)))"
            result.foldPreOrder(List<Int>()) { i -> { l1 -> { l2 -> List(i).concat(l1).concat(l2) } } }.toString() shouldBe "[4, 2, 1, 3, 6, 5, 7, NIL]"
        }

        "testTreeFold" {
            val list = List(4, 6, 7, 5, 2, 1, 3)
            val tree0 = Tree(list)
            val tree1 = tree0.foldInOrder(Tree<Int>()) { t1 -> { i -> { t2 -> Tree(t1, i, t2) } } }
            tree1.toString() shouldBe tree0.toString()
            val tree2 = tree0.foldPostOrder(Tree<Int>()) { t1 -> { t2 -> { i -> Tree(t1, i, t2) } } }
            tree2.toString() shouldBe tree0.toString()
            val tree3 = tree0.foldPreOrder(Tree<Int>()) { i -> { t1 -> { t2 -> Tree(t1, i, t2) } } }
            tree3.toString() shouldBe tree0.toString()
        }
    }
}
