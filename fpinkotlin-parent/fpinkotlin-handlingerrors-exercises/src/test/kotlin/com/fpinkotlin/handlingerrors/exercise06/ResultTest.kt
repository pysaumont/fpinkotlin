package com.fpinkotlin.handlingerrors.exercise06

import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec


class ResultTest: StringSpec() {

    init {

        "exists" {
            forAll { z: Int ->
                Result(z).exists { it % 2 == 0 } == (z % 2 == 0)
            }
        }
    }
}
