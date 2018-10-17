package com.fpinkotlin.trees.exercise07



import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    init {

        "merge" {
            forAll { list1: List<Int>, list2: List<Int> ->
                val tree1 = list1.fold(Tree<Int>()) { tree, elem ->  tree + elem }
                val tree2 = list2.fold(Tree<Int>()) { tree, elem ->  tree + elem }
                list1.asSequence().plus(list2).all { i: Int -> tree1.merge(tree2).contains(i) }
            }
        }
    }

}
