package com.fpinkotlin.handlingerrors.exercise03

import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class EitherTest: StringSpec() {

    init {
        "getOrElse" {
            forAll { n: Int, m: Int ->
                Either.right<String, Int>(n).getOrElse { m } == n &&
                    Either.left<String, Int>("Error").getOrElse { m } == m
            }
        }

        "orElse" {
            forAll { n: Int, m: Int ->
                val erm: Either<String, Int> = Either.right(m)
                Either.right<String, Int>(n).orElse { erm }.toString() == Either.right<String, Int>(n).toString() &&
                    Either.left<String, Int>("Error").orElse { erm } == erm
            }
        }
    }
}
