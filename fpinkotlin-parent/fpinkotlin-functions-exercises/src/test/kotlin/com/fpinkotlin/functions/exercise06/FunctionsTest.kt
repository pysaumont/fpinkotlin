package com.fpinkotlin.functions.exercise06


import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    init {

        "compose" {
            forAll(Gen.int(), { x ->
                TODO("Implement the value function higherAndThen, then remove this line an uncomment the following line")
                // higherAndThen<Int, Int, Int>()(::square)(::triple)(x) == triple(square(x))
            })
        }
    }
}

