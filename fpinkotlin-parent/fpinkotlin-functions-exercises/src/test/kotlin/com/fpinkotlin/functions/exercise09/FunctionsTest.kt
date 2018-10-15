package com.fpinkotlin.functions.exercise09


import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    init {

        "curried" {
            forAll { a: Int, b: Double, c: String, d: Boolean ->
                curried<Int, Double, String, Boolean>()(a)(b)(c)(d) == "$a, $b, $c, $d"
            }
        }
    }
}
