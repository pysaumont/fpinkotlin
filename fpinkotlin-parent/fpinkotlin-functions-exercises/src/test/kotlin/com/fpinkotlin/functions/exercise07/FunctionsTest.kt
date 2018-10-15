package com.fpinkotlin.functions.exercise07


import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    private val f = { a: Int -> { b: Double -> a * (1 + b / 100) } }

    init {

        "partialA" {
            forAll { x: Int, y: Double ->
                y.isNaN() || y.isInfinite() || partialA(x, f)(y) == f(x)(y)
            }
        }
    }
}

