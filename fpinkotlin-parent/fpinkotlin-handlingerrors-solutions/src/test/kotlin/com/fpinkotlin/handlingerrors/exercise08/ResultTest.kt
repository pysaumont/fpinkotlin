package com.fpinkotlin.handlingerrors.exercise08


import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class ResultTest: StringSpec() {

    init {

        "invokeWithMessage" {
            forAll { z: Int ->
                val errorMessage = "Value is odd"
                Result(if (z % 2 == 0) z else null, errorMessage).toString() == when {
                    z % 2 == 0 -> Result(z).toString()
                    else -> Result.failure<Int>(errorMessage).toString()
                }
            }
        }

        "invokeWithPredicate" {
            forAll { z: Int ->
                Result(z) { it % 2 == 0 }.toString() == when {
                    z % 2 == 0 -> Result(z).toString()
                    else -> Result<Int>().toString()
                }
            }
        }

        "invokeWithPredicateAndMessage" {
            forAll { z: Int ->
                val errorMessage = "Value is odd"
                Result(z, errorMessage) { it % 2 == 0 }.toString() == when {
                    z % 2 == 0 -> Result(z).toString()
                    else -> Result.failure<Int>("Argument $z does not match condition: $errorMessage").toString()
                }
            }
        }
    }
}
