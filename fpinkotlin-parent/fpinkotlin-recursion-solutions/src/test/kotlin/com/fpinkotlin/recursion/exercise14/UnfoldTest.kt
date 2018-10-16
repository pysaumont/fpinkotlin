package com.fpinkotlin.recursion.exercise14


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class UnfoldTest : StringSpec() {

    init {

        "unfold" {
            forAll(Gen.choose(0, 10_000), Gen.choose(0, 10_000)) { a: Int, b: Int ->
                a > b || unfold(a, { x -> x + 1}) { x -> x < b }  == (a until b).toList()
            }
        }
    }
}