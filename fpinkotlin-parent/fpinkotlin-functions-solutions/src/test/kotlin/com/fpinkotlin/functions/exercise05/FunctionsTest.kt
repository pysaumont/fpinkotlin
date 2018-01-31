package com.fpinkotlin.functions.exercise05


import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    init {

        "compose" {
            forAll(Gen.int(), { x ->
                higherCompose<Int, Int, Int>()(::square)(::triple)(x) == square(triple(x))
            })
        }
    }
}

