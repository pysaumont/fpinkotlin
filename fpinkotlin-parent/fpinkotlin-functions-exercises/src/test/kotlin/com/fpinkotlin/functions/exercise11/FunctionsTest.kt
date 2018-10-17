package com.fpinkotlin.functions.exercise11


import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    private val f = { a: Int, b: Double -> a * (1 + b / 100) }

    init {

        "swapArgs" {
            forAll { x: Int, y: Double ->
                y.isNaN() || y.isInfinite() || swapArgs(curry(f))(y)(x) == f(x, y)
            }
        }
    }
}
