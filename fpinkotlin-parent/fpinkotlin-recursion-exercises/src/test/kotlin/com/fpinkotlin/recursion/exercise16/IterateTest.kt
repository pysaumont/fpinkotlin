package com.fpinkotlin.recursion.exercise16


import com.fpinkotlin.generators.IntPairGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class IterateTest : StringSpec() {

    init {

        "iterate" {
            forAll(IntPairGenerator(), { (a, b) ->
                iterate(a, { x -> x + 1}, b - a)  == (a until b).toList()
            })
        }
    }
}