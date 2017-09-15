package com.fpinkotlin.functions.exercise04


import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    init {

        "compose" {
            forAll(Gen.int(), { x ->
                TODO("Implement the value function compose, then remove this line an uncomment the following line")
                //compose(::square)(::triple)(x) == square(triple(x))
            })
        }
    }
}

