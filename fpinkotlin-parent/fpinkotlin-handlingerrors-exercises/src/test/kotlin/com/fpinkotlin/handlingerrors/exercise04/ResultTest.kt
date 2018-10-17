package com.fpinkotlin.handlingerrors.exercise04

import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec
import java.io.IOException
import java.util.*

class ResultTest: StringSpec() {

    private val random = Random()

    init {

        "map" {
            forAll { n: Int ->
                Result(n).map { it / 2.0 }.toString() ==
                    Result(n / 2.0).toString()
            }
        }

        "mapException" {
            forAll { n: Int ->
                Result(n).map { throw IOException("IOException") }.toString() ==
                    Result.failure<Int>(IOException("IOException")).toString()
            }
        }

        "mapRuntimeException" {
            forAll { n: Int ->
                Result(n).map { throw IllegalStateException("IllegalStateException") }.toString() ==
                    Result.failure<Int>(IllegalStateException("IllegalStateException")).toString()
            }
        }

        "flatMap" {
            forAll { n: Int ->
                Result(n).flatMap { Result(it / 2.0) }.toString() ==
                    Result(n / 2.0).toString()
            }
        }

        "flatMapException" {
            forAll { n: Int ->
                Result(n).flatMap<Int> { throw IOException("IOException") }.toString() ==
                    Result.failure<Int>(IOException("IOException")).toString()
            }
        }

        "flatMapRuntimeException" {
            forAll { n: Int ->
                Result(n).flatMap<Int> { throw IllegalStateException("IllegalStateException") }.toString() ==
                    Result.failure<Int>(IllegalStateException("IllegalStateException")).toString()
            }
        }

        "getOrElse" {
            forAll { x: Int, z: Int ->
                val f = { x }
                Result(z).getOrElse(f) == z &&
                    Result.failure<Int>("Error").getOrElse(f) == x
            }
        }

        "orElse" {
            forAll { z: Int, y: Int ->
                val x: Result<Int> = Result(y)
                val f = { x }
                Result(z).orElse(f).toString() == Result(z).toString() &&
                    Result.failure<Int>("Error").orElse(f) == x
            }
        }

        "orElseException" {
            val f = { throw IOException("IOException")  }
            forAll { z: Int ->
                Result(z).orElse(f).toString() == Result(z).toString() &&
                    Result.failure<Int>("Error").orElse(f).toString() == Result.failure<Int>(IOException("IOException")).toString()
            }
        }

        "orElseRuntimeException" {
            val f = { throw IllegalStateException("IllegalStateException")  }
            forAll { z: Int ->
                Result(z).orElse(f).toString() == Result(z).toString() &&
                    Result.failure<Int>("Error").orElse(f).toString() == Result.failure<Int>(IllegalStateException("IllegalStateException")).toString()
            }
        }
    }
}
