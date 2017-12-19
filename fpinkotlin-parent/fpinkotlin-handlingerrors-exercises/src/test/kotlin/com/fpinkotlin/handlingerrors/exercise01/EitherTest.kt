package com.fpinkotlin.handlingerrors.exercise01

import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class EitherTest: StringSpec() {

    init {

        "mapRight" {
            forAll(Gen.int(), { z ->
                Either.right<String, Int>(z).map { it / 2.0 }.toString() ==
                        Either.right<String, Double>(z / 2.0).toString()
            })
        }

        "mapLeft" {
            forAll(Gen.int(), { z ->
                Either.left<String, Int>("Error").map { it / 2.0 }.toString() ==
                        Either.left<String, Double>("Error").toString()
            })
        }
    }
}
