package com.fpinkotlin.functions.exercise08


import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    private val f = { a: Int -> { b: Double -> a * (1 + b / 100) } }

    init {

// Uncomment after implementing the function
//        "partialB" {
//            forAll { x: Int, y: Double ->
//                y.isNaN() || y.isInfinite() || partialB(y, f)(x) == f(x)(y)
//            }
//        }
    }
}
