package com.fpinkotlin.functions.exercise09


import com.fpinkotlin.generators.IntDoublePairGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    init {

        "curried" {
            forAll(IntDoublePairGenerator(), { (a, b) ->
                TODO("Implement function currying, then remove this line an uncomment the following lines")
                // val c = Gen.string().generate()
                // val d = Gen.bool().generate()
                // curried<Int, Double, String, Boolean>()(a)(b)(c)(d) == "$a, $b, $c, $d"
            })
        }
    }
}
