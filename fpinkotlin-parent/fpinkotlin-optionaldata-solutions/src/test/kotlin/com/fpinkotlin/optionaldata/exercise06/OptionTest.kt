package com.fpinkotlin.optionaldata.exercise06

import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class OptionTest: StringSpec() {

    init {

        "filter" {
            val p: (Int) -> Boolean = { it % 5 != 0 }
            forAll(Gen.int(), { z ->
                Option(z).filter(p) == if (p(z)) Option(z) else Option()
            })
        }
    }
}
