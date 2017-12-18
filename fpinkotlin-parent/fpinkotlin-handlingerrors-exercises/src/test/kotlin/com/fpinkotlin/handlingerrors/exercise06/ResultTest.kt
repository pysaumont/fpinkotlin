package com.fpinkotlin.handlingerrors.exercise06

import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec


class ResultTest: StringSpec() {

    init {

        "exists" {
            forAll(Gen.int(), { z ->
                Result(z).exists { it % 2 == 0 } == (z % 2 == 0)
            })
        }
    }
}
