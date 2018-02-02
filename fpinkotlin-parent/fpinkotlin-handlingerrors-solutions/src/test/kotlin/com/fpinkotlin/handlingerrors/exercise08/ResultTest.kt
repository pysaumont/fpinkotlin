package com.fpinkotlin.handlingerrors.exercise08


import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ResultTest: StringSpec() {

    init {

        "invokeWithMessage" {
            forAll(Gen.int(), { z ->
                val errorMessage = "Value is odd"
                Result(if (z % 2 == 0) z else null, errorMessage).toString() == when {
                    z % 2 == 0 -> Result(z).toString()
                    else -> Result.failure<Int>(errorMessage).toString()
                }
            })
        }

        "invokeWithPredicate" {
            forAll(Gen.int(), { z ->
                Result(z) { it % 2 == 0 }.toString() == when {
                    z % 2 == 0 -> Result(z).toString()
                    else -> Result<Int>().toString()
                }
            })
        }

        "invokeWithPredicateAndMessage" {
            forAll(Gen.int(), { z ->
                val errorMessage = "Value is odd"
                Result(z, errorMessage) { it % 2 == 0 }.toString() == when {
                    z % 2 == 0 -> Result(z).toString()
                    else -> Result.failure<Int>("Argument $z does not match condition: $errorMessage").toString()
                }
            })
        }
    }
}
