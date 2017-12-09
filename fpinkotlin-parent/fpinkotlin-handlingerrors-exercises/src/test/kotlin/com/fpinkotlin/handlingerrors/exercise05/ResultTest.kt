package com.fpinkotlin.handlingerrors.exercise05

import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec


class ResultTest: StringSpec() {

    init {

        "filter" {
            forAll(Gen.int(), { z ->
                Result(z).filter { it % 2 == 0 }.toString() == when {
                    z % 2 == 0 -> Result(z).toString()
                    else -> Result(0).filter { false }.toString()
                }
            })
        }

        "filterWithMessage" {
            forAll(Gen.int(), { z ->
                val errorMessage = "Value is odd"
                Result(z).filter(errorMessage) { it % 2 == 0 }.toString() == when {
                    z % 2 == 0 -> Result(z).toString()
                    else -> Result.failure<Int>(errorMessage).toString()
                }
            })
        }
    }
}
