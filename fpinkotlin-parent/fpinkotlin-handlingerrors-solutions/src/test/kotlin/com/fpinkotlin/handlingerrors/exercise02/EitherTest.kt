package com.fpinkotlin.handlingerrors.exercise02

import io.kotlintest.properties.forAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class EitherTest: StringSpec() {

    init {

        "flatMapRight" {
            forAll { n: Int ->
                Either.right<String, Int>(n).flatMap { Either.right<String, Double>(it / 2.0) }.toString() ==
                    Either.right<String, Double>(n / 2.0).toString()
            }
        }

        "flatMapLeftRight" {
            Either.left<String, Int>("Error").flatMap { Either.right<String, Double>(it / 2.0) }.toString() shouldBe
                Either.left<String, Double>("Error").toString()
        }

        "flatMapLeftLeft" {
            Either.left<String, Int>("Error1").flatMap { Either.left<String, Double>("Error2") }.toString() shouldBe
                Either.left<String, Double>("Error1").toString()
        }
    }
}
