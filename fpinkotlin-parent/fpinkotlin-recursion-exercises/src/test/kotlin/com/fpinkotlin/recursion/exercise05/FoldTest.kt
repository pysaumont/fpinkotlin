package com.fpinkotlin.recursion.exercise05

import com.fpinkotlin.generators.CharListGenerator
import com.fpinkotlin.generators.IntListGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class FoldTest : StringSpec() {

    init {

        "string" {
            forAll(CharListGenerator(), { (array, list) ->
                string(list) == array.fold("") { s, c -> s + c}
            })
        }
    }

    init {

        "sum" {
            forAll(IntListGenerator(), { (array, list) ->
                sum(list) == array.fold(0) { s, c -> s + c}
            })
        }
    }
}