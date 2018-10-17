package com.fpinkotlin.functions.exercise10


import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    private val f = { a: Int, b: Double -> a * (1 + b / 100) }

    init {

        "curry" {
            forAll { x: Int, y: Double ->
                y.isNaN() || y.isInfinite() || curry(f)(x)(y) == f(x, y)
            }
        }
    }
}
