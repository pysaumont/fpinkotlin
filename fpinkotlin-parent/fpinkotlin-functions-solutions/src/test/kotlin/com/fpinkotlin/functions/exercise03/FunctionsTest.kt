package com.fpinkotlin.functions.exercise03


import com.fpinkotlin.generators.IntPairGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    init {

        "add" {
            forAll(IntPairGenerator(), { (x, y) ->
                add(x)(y) == x + y
            })
        }
    }
}

