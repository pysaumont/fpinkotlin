package com.fpinkotlin.functions.exercise05


import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    init {

        "compose" {
            forAll(Gen.int(), { x ->
                TODO("Implement the value function higherCompose, then remove this line an uncomment the following line")
                // higherCompose<Int, Int, Int>()(::square)(::triple)(x) == square(triple(x))
            })
        }
    }
}

