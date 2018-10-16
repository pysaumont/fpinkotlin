package com.fpinkotlin.optionaldata.exercise08

import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class OptionTest: StringSpec() {

    init {

        "lift" {
            val p: (Int) -> Boolean = { it % 5 != 0 }
            forAll { z: Int ->
                lift(p)(Option(z)) == Option(p(z))
            }
        }
    }
}
