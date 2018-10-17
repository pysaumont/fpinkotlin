package com.fpinkotlin.optionaldata.exercise10

import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class OptionTest: StringSpec() {

    init {

        "map2" {
            val p: (Int) -> (Int) -> Int = { a -> { b -> a * b } }
            forAll { x: Int, y: Int ->
                map2(Option(x), Option(y), p) == Option(p(x)(y))
            }
        }
    }
}