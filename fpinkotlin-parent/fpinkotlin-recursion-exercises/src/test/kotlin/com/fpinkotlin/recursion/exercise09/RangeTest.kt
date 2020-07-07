package com.fpinkotlin.recursion.exercise09


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class RangeTest : StringSpec() {

    init {

        "range" {
            forAll(Gen.choose(0, 500), Gen.choose(0, 500)) { a: Int, b: Int ->
                range(a, a + b) == (a until a + b).toList()
            }
        }
    }
}