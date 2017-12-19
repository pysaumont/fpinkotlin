package com.fpinkotlin.optionaldata.exercise08

import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class OptionTest: StringSpec() {

    init {

        "lift" {
            val p: (Int) -> Boolean = { it % 5 != 0 }
            forAll(Gen.int(), { z ->
                lift(p)(Option(z)) == Option(p(z))
            })
        }
    }
}
