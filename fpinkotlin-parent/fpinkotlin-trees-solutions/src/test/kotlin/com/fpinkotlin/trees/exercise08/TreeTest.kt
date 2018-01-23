package com.fpinkotlin.trees.exercise08


import com.fpinkotlin.generators.IntListGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    init {

        "foldLeft" {
            forAll(IntListGenerator(), { (array, list) ->
                val tree = list.foldLeft(Tree<Int>()) { tree -> { elem ->  tree + elem } }
                val f = { a: Int -> { b: Int -> a + b } }
                tree.foldLeft(0, f, f) == array.sum()
            })
        }
    }

}
