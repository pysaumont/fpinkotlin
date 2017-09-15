package com.fpinkotlin.functions.exercise07


import com.fpinkotlin.generators.IntDoublePairGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    private val f = { a: Int -> { b: Double -> a * (1 + b / 100) } }

    init {

        "partialA" {
            forAll(IntDoublePairGenerator(), { (x, y) ->
                partialA(x, f)(y) == f(x)(y)
            })
        }
    }
}
