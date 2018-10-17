package com.fpinkotlin.functions.exercise04


import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    init {

        "compose4" {
            forAll { x: Int ->
                compose(::square)(::triple)(x) == square(triple(x))
            }
        }
    }
}

