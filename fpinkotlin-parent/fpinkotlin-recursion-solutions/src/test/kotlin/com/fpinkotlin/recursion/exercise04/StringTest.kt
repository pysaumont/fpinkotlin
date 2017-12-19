package com.fpinkotlin.recursion.exercise04

import com.fpinkotlin.generators.CharListGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class StringTest : StringSpec() {

    init {

        "string" {
            forAll(CharListGenerator(), { (array, list) ->
                string(list) == array.fold("") { s, c -> s + c}
            })
        }
    }
}