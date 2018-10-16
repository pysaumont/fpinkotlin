package com.fpinkotlin.recursion.exercise16


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class IterateTest : StringSpec() {

    init {

        "iterate" {
            forAll(Gen.choose(0, 1000), Gen.choose(0, 1000)) { a: Int, b: Int ->
                a > b || iterate(a, { x -> x + 1}, b - a)  == (a until b).toList()
            }
        }
    }
}