package com.fpinkotlin.optionaldata.exercise09

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class OptionTest: StringSpec() {

    init {

        "lift" {
            val p: (Int) -> Int = { if (it % 5 == 0) throw RuntimeException("Should not be seen") else it }
            forAll(Gen.int()) { z ->
                lift(p)(Option(z)) == if (z % 5 != 0) Option(p(z)) else Option()
            }
        }
    }
}