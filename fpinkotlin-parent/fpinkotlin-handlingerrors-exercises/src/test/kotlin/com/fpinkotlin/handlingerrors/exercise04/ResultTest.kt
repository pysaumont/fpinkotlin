package com.fpinkotlin.handlingerrors.exercise04

import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import java.io.IOException

class ResultTest: StringSpec() {

    init {

        "map" {
            forAll(Gen.int(), { z ->
                Result(z).map { it / 2.0 }.toString() ==
                    Result(z / 2.0).toString()
            })
        }

        "mapException" {
            forAll(Gen.int(), { z ->
                Result(z).map { throw IOException("IOException") }.toString() ==
                    Result.failure<Int>(IOException("IOException")).toString()
            })
        }

        "mapRuntimeException" {
            forAll(Gen.int(), { z ->
                Result(z).map { throw IllegalStateException("IllegalStateException") }.toString() ==
                    Result.failure<Int>(IllegalStateException("IllegalStateException")).toString()
            })
        }

        "flatMap" {
            forAll(Gen.int(), { z ->
                Result(z).flatMap { Result(it / 2.0) }.toString() ==
                    Result(z / 2.0).toString()
            })
        }

        "flatMapException" {
            forAll(Gen.int(), { z ->
                Result(z).flatMap<Int> { throw IOException("IOException") }.toString() ==
                    Result.failure<Int>(IOException("IOException")).toString()
            })
        }

        "flatMapRuntimeException" {
            forAll(Gen.int(), { z ->
                Result(z).flatMap<Int> { throw IllegalStateException("IllegalStateException") }.toString() ==
                    Result.failure<Int>(IllegalStateException("IllegalStateException")).toString()
            })
        }

        "getOrElse" {
            val x = Gen.int().generate()
            val f = { x }
            forAll(Gen.int(), { z ->
                Result(z).getOrElse(f) == z &&
                    Result.failure<Int>("Error").getOrElse(f) == x
            })
        }

        "orElse" {
            val x: Result<Int> = Result(Gen.int().generate())
            val f = { x }
            forAll(Gen.int(), { z ->
                Result(z).orElse(f).toString() == Result(z).toString() &&
                    Result.failure<Int>("Error").orElse(f) == x
            })
        }

        "orElseException" {
            val x: Result<Int> = Result(Gen.int().generate())
            val f = { throw IOException("IOException")  }
            forAll(Gen.int(), { z ->
                Result(z).orElse(f).toString() == Result(z).toString() &&
                    Result.failure<Int>("Error").orElse(f).toString() == Result.failure<Int>(IOException("IOException")).toString()
            })
        }

        "orElseRuntimeException" {
            val x: Result<Int> = Result(Gen.int().generate())
            val f = { throw IllegalStateException("IllegalStateException")  }
            forAll(Gen.int(), { z ->
                Result(z).orElse(f).toString() == Result(z).toString() &&
                    Result.failure<Int>("Error").orElse(f).toString() == Result.failure<Int>(IllegalStateException("IllegalStateException")).toString()
            })
        }
    }
}