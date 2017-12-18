package com.fpinkotlin.handlingerrors.exercise07

import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ResultTest: StringSpec() {

    init {

        "filter" {
            forAll(Gen.int(), { z ->
                val errorMessage = "New error message"
                Result(z).filter { it % 2 == 0 }.mapFailure(errorMessage).toString() == when {
                    z % 2 == 0 -> Result(z).toString()
                    else -> Result.failure<Int>(errorMessage).toString()
                }
            })
        }
    }
}
