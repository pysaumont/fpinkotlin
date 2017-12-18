package com.fpinkotlin.functions.exercise10


import com.fpinkotlin.generators.IntDoublePairGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    private val f = { a: Int, b: Double -> a * (1 + b / 100) }

    init {

        "curry" {
            forAll(IntDoublePairGenerator(), { (x, y) ->
                curry(f)(x)(y) == f(x, y)
            })
        }
    }
}
