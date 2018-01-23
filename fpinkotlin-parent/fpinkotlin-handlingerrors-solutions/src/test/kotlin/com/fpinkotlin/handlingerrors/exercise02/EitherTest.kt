package com.fpinkotlin.handlingerrors.exercise02

import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class EitherTest: StringSpec() {

    init {

        "flatMapRight" {
            forAll(Gen.int(), {
                Either.right<String, Int>(it).flatMap { Either.right<String, Double>(it / 2.0) }.toString() ==
                        Either.right<String, Double>(it / 2.0).toString()
            })
        }

        "flatMapLeftRight" {
            forAll(Gen.int(), {
                Either.left<String, Int>("Error").flatMap { Either.right<String, Double>(it / 2.0) }.toString() ==
                        Either.left<String, Double>("Error").toString()
            })
        }

        "flatMapLeftLeft" {
            forAll(Gen.int(), {
                Either.left<String, Int>("Error1").flatMap { Either.left<String, Double>("Error2") }.toString() ==
                        Either.left<String, Double>("Error1").toString()
            })
        }
    }
}
