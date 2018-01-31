package com.fpinkotlin.optionaldata.exercise03

import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class OptionTest: StringSpec() {

    init {

        "map" {
            forAll(Gen.int(), { x ->
                Option(x).map { it.toString() } == Option(x.toString())
            })
        }
    }
}
