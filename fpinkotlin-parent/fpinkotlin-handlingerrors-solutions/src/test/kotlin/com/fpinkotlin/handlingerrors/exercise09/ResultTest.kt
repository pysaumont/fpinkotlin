package com.fpinkotlin.handlingerrors.exercise09


import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class ResultTest: StringSpec() {

    init {

        "forEach" {
            forAll { z: Int ->
                val errorMessage = "Value is odd"
                var result = false
                Result(if (z % 2 == 0) z else null, errorMessage).forEach { x -> result = (x == z)}
                result || z % 2 != 0
            }
        }
    }
}
