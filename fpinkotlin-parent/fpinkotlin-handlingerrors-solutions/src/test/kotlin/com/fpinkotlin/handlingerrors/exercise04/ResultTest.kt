package com.fpinkotlin.handlingerrors.exercise04

import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ResultTest: StringSpec() {

    init {

        "map" {
            forAll(Gen.int(), { z ->
                Result(z).map { it / 2.0 }.toString() ==
                        Result(z / 2.0).toString()
            })
        }

        "flatMap" {
            forAll(Gen.int(), { z ->
                Result(z).flatMap { Result(it / 2.0) }.toString() ==
                        Result(z / 2.0).toString()
            })
        }

        "getOrElse" {
            val x = Gen.int().generate()
            forAll(Gen.int(), { z ->
                Result(z).getOrElse { x } == z &&
                        Result.failure<Int>("Error").getOrElse { x } == x
            })
        }

        "orElse" {
            val x: Result<Int> = Result(Gen.int().generate())
            forAll(Gen.int(), { z ->
                Result(z).orElse { x }.toString() == Result(z).toString() &&
                        Result.failure<Int>("Error").orElse { x } == x
            })
        }
    }
}
