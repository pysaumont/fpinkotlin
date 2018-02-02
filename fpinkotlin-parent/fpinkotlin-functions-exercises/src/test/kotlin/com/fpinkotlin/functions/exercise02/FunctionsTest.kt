package com.fpinkotlin.functions.exercise02

import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    init {

        "compose" {
            forAll(Gen.int(), { x ->
                TODO("Implement generic version of compose, then remove this line an uncomment the test")
                // compose(::square, ::triple)(x) == square(triple(x))
            })
        }
    }
}