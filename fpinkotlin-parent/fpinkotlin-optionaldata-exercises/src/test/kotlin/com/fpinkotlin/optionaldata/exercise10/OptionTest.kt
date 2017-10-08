package com.fpinkotlin.optionaldata.exercise10

import com.fpinkotlin.generators.IntPairGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class OptionTest: StringSpec() {

    init {

        "map2" {
            val p: (Int) -> (Int) -> Int = { a -> { b -> a * b } }
            forAll(IntPairGenerator(), { (x,y) ->
                map2(Option(x), Option(y), p) == Option(p(x)(y))
            })
        }
    }
}