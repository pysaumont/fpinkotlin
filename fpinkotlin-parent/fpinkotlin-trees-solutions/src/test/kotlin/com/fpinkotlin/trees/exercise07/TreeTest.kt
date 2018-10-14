package com.fpinkotlin.trees.exercise07


import com.fpinkotlin.generators.IntKListPairGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    init {

        "merge" {
            forAll(IntKListPairGenerator(), { (list1, list2) ->
                val tree1 = list1.fold(Tree<Int>()) { tree, elem ->  tree + elem }
                val tree2 = list2.fold(Tree<Int>()) { tree, elem ->  tree + elem }
                list1.asSequence().plus(list2).all { i: Int -> tree1.merge(tree2).contains(i) }
            })
        }
    }

}
