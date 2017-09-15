package com.fpinkotlin.functions.exercise09


import com.fpinkotlin.generators.IntDoublePairGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    init {

        "curried" {
            forAll(IntDoublePairGenerator(), { (a, b) ->
                val c = Gen.string().generate()
                val d = Gen.bool().generate()
                curried<Int, Double, String, Boolean>()(a)(b)(c)(d) == "$a, $b, $c, $d"
            })
        }
    }
}
