package com.fpinkotlin.recursion.exercise13


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class UnfoldTest : StringSpec() {

    init {

        "unfold" {
            forAll(10, Gen.choose(0, 1_000), Gen.choose(0, 1_000)) { a: Int, b: Int ->
                a > b || unfold(a, { x -> x + 1}) { x -> x < b }  == (a until b).toList()
            }
        }
    }
}