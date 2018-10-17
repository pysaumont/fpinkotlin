package com.fpinkotlin.functions.exercise05


import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    init {

        "higherCompose" {
            forAll { x: Int ->
                higherCompose<Int, Int, Int>()(::square)(::triple)(x) == square(triple(x))
            }
        }
    }
}

