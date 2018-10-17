package com.fpinkotlin.recursion.exercise09


import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class RangeTest : StringSpec() {

    init {

        "range" {
            forAll { a: Int, b: Int ->
                range(a, b) == (a until b).toList()
            }
        }
    }
}