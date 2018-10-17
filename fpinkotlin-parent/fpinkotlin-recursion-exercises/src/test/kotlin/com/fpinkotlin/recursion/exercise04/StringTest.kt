package com.fpinkotlin.recursion.exercise04

import com.fpinkotlin.generators.CharKListGenerator
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class StringTest : StringSpec() {

    init {

        "string" {
            forAll(CharKListGenerator()) { list ->
                string(list) == list.toCharArray().fold("") { s, c -> s + c}
            }
        }
    }
}

