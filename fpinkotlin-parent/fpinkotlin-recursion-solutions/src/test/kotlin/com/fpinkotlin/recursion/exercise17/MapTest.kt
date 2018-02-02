package com.fpinkotlin.recursion.exercise17


import com.fpinkotlin.generators.IntPairGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class RangeTest : StringSpec() {

    init {

        "range" {
            forAll(IntPairGenerator(), { (a, b) ->
                map((a until b).toList()) { it * 2} == (a * 2 until b * 2 step 2).toList()
            })
        }
    }
}