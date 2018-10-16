package com.fpinkotlin.handlingerrors.exercise11


import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class ResultTest: StringSpec() {

    init {

        "forEachSuccessFailure" {
            forAll { z: Int ->
                val errorMessage = "Value is odd"
                var result = false
                Result(if (z % 2 == 0) z else null, errorMessage).forEach({ x -> result = (x == z)}, { e -> result = (e.message
                    == errorMessage)})
                result
            }
        }

        "forEachSuccessEmpty" {
            forAll { z: Int ->
                var result = false
                (if (z % 2 == 0) Result(z) else Result()).forEach({ x -> result = (x == z)}, { throw it }, { result = true })
                result
            }
        }

        "forEachSuccessEmptyNamedArguments" {
            forAll { z: Int ->
                var result = false
                (if (z % 2 == 0) Result(z) else Result()).forEach({ x -> result = (x == z)}, onEmpty = { result =
                    true })
                result
            }
        }
    }
}
