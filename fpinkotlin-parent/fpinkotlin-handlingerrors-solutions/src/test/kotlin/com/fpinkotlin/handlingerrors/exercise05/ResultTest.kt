package com.fpinkotlin.handlingerrors.exercise05

import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec


class ResultTest: StringSpec() {

    init {

        "filter" {
            forAll { z: Int ->
                Result(z).filter { it % 2 == 0 }.toString() == when {
                    z % 2 == 0 -> Result(z).toString()
                    else -> Result(0).filter { false }.toString()
                }
            }
        }

        "filterWithMessage" {
            forAll { z: Int ->
                val errorMessage = "Value is odd"
                Result(z).filter(errorMessage) { it % 2 == 0 }.toString() == when {
                    z % 2 == 0 -> Result(z).toString()
                    else -> Result.failure<Int>(errorMessage).toString()
                }
            }
        }
    }
}
