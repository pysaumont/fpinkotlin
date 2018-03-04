package com.fpinkotlin.functions.exercise04


import com.fpinkotlin.functions.exercise04.error.square
import com.fpinkotlin.functions.exercise04.error.triple
import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    init {

        "compose" {
            forAll(Gen.int(), { x ->
                compose(::square)(::triple)(x) == square(triple(x))
            })
        }
    }
}

