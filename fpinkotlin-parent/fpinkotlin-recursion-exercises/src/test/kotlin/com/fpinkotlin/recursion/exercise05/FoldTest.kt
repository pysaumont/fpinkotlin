package com.fpinkotlin.recursion.exercise05

import com.fpinkotlin.generators.CharKListGenerator
import com.fpinkotlin.generators.IntKListGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class FoldTest : StringSpec() {

    init {

        "string" {
            forAll(CharKListGenerator(), { (array, list) ->
                string(list) == array.fold("") { s, c -> s + c}
            })
        }
    }

    init {

        "sum" {
            forAll(IntKListGenerator(), { (array, list) ->
                sum(list) == array.fold(0) { s, c -> s + c}
            })
        }
    }
}