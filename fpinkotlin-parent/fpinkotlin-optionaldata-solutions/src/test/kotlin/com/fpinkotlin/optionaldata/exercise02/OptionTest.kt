package com.fpinkotlin.optionaldata.exercise02

import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class OptionTest: StringSpec() {

    init {

        "getOrElse" {
            val x = Gen.int().generate()
            val y: Int? = null
            forAll(Gen.int(), { z ->
                Option(z).getOrElse { x } == z && Option(y).getOrElse { x } == x
            })
        }
    }
}
