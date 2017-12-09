package com.fpinkotlin.handlingerrors.exercise09


import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ResultTest: StringSpec() {

    init {

        "forEach" {
            forAll(Gen.int(), { z ->
                val errorMessage = "Value is odd"
                var result = false
                Result(if (z % 2 == 0) z else null, errorMessage).forEach { x -> result = (x == z)}
                result || z % 2 != 0
            })
        }
    }
}
