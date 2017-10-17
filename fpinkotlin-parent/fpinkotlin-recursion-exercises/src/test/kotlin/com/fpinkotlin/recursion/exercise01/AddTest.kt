package com.fpinkotlin.recursion.exercise01

import com.fpinkotlin.generators.IntPairGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class AddTest: StringSpec() {

    init {

        "add" {
            forAll(IntPairGenerator(), { (a, b) ->
                add(a, b) == a + b
            })
        }
    }
}
