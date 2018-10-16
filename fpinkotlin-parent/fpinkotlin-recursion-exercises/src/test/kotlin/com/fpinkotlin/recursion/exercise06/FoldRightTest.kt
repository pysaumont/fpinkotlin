package com.fpinkotlin.recursion.exercise06

import com.fpinkotlin.generators.CharKListGenerator
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class FoldRightTest : StringSpec() {

    init {

        "string" {
            forAll(CharKListGenerator()) { list ->
                string(list) == list.toCharArray().fold("") { s, c -> s + c}
            }
        }
    }

    init {

        "sum" {
            forAll { list: List<Int> ->
                sum(list) == list.toIntArray().fold(0) { s, c -> s + c}
            }
        }
    }
}