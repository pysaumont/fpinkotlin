package com.fpinkotlin.trees.exercise05

import com.fpinkotlin.common.getOrElse
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    init {

        "max0" {
            val tree = Tree<Int>()
            tree.max().getOrElse(false) shouldBe false
        }

        "max1" {
            val tree = Tree<Int>() + 4 + 2 + 6 + 1 + 3 + 7 + 5
            tree.max().map { i -> i == 7 }.getOrElse(false) shouldBe true
        }

        "max2" {
            val tree = Tree<Int>() + 1 + 2 + 3 + 4 + 5 + 6 + 7
            tree.max().map { i -> i == 7 }.getOrElse(false) shouldBe true
        }

        "min0" {
            val tree = Tree<Int>()
            tree.min().getOrElse(false) shouldBe false
        }

        "min1" {
            val tree = Tree<Int>() + 4 + 2 + 6 + 1 + 3 + 7 + 5
            tree.min().map { i -> i == 1 }.getOrElse(false) shouldBe true
        }

        "min2" {
            val tree = Tree<Int>() + 1 + 2 + 3 + 4 + 5 + 6 + 7
            tree.min().map { i -> i == 1 }.getOrElse(false) shouldBe true
        }
    }

}