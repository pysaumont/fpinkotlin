package com.fpinkotlin.optionaldata.exercise06

import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class OptionTest: StringSpec() {

    init {

        "filter" {
            val p: (Int) -> Boolean = { it % 5 != 0 }
            forAll { z: Int ->
                Option(z).filter(p) == if (p(z)) Option(z) else Option()
            }
        }
    }
}
