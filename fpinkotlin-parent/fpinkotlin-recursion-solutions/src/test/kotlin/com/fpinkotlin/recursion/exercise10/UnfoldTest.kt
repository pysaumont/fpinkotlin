package com.fpinkotlin.recursion.exercise10


import com.fpinkotlin.generators.IntPairGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class UnfoldTest : StringSpec() {

    init {

        "unfold" {
            forAll(IntPairGenerator(), { (a, b) ->
                unfold(a, { x -> x + 1}) { x -> x <b}  == (a until b).toList()
            })
        }
    }
}