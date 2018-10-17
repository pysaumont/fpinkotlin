package com.fpinkotlin.handlingerrors.exercise01

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class EitherTest: StringSpec() {

    init {

        "mapRight" {
            forAll(Gen.int()) { n ->
                Either.right<String, Int>(n).map { it / 2.0 }.toString() ==
                    Either.right<String, Double>(n / 2.0).toString()
            }
        }

        "mapLeft" {
            forAll(Gen.int()) { n ->
                Either.left<String, Int>("Error").map { it / 2.0 }.toString() ==
                    Either.left<String, Double>("Error").toString()
            }
        }
    }
}
