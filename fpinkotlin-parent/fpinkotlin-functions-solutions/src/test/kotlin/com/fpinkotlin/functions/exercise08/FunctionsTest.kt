package com.fpinkotlin.functions.exercise08


import com.fpinkotlin.generators.IntDoublePairGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    private val f = { a: Int -> { b: Double -> a * (1 + b / 100) } }

    init {

        "partialB" {
            forAll(IntDoublePairGenerator(), { (x, y) ->
                partialB(y, f)(x) == f(x)(y)
            })
        }
    }
}
