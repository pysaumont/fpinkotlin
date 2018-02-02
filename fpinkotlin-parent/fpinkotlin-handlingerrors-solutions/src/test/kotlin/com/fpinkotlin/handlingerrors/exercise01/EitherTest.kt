package com.fpinkotlin.handlingerrors.exercise01

import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class EitherTest: StringSpec() {

    init {

        "mapRight" {
            forAll(Gen.int(), {
                Either.right<String, Int>(it).map { it / 2.0 }.toString() ==
                        Either.right<String, Double>(it / 2.0).toString()
            })
        }

        "mapLeft" {
            forAll(Gen.int(), {
                Either.left<String, Int>("Error").map { it / 2.0 }.toString() ==
                        Either.left<String, Double>("Error").toString()
            })
        }
    }
}
