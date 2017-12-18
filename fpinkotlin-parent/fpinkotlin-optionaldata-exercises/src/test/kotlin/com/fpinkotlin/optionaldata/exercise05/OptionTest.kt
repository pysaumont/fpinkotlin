package com.fpinkotlin.optionaldata.exercise05

import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class OptionTest: StringSpec() {

    init {

        "orElse" {
            val x: Option<Int> = Option(Gen.int().generate())
            val y: Int? = null
            forAll(Gen.int(), { z ->
                Option(z).orElse { x } == Option(z) && Option(y).orElse { x } == x
            })
        }
    }
}
