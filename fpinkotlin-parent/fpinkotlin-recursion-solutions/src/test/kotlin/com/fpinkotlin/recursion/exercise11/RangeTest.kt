package com.fpinkotlin.recursion.exercise11


import com.fpinkotlin.generators.IntPairGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class RangeTest : StringSpec() {

    init {

        "range" {
            forAll(IntPairGenerator(), { (a, b) ->
                range(a, b) == (a until b).toList()
            })
        }
    }
}