package com.fpinkotlin.functions.exercise11


import com.fpinkotlin.generators.IntDoublePairGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    private val f = { a: Int, b: Double -> a * (1 + b / 100) }

    init {

        "swapArgs" {
            forAll(IntDoublePairGenerator(), { (x, y) ->
                swapArgs(curry(f))(y)(x) == f(x, y)
            })
        }
    }
}
