package com.fpinkotlin.functions.exercise06


import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    init {

        "higherAndThen" {
            forAll { x: Int ->
                higherAndThen<Int, Int, Int>()(::square)(::triple)(x) == triple(square(x))
            }
        }
    }
}

