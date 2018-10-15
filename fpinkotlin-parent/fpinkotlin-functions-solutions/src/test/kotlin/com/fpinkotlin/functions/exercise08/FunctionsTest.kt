package com.fpinkotlin.functions.exercise08


import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    private val f = { a: Int -> { b: Double -> a * (1 + b / 100) } }

    init {

        "partialB" {
            forAll { x: Int, y: Double ->
                y.isNaN() || y.isInfinite() || partialB(y, f)(x) == f(x)(y)
            }
        }
    }
}
