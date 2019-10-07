package com.fpinkotlin.recursion.exercise01

import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "add" {
            forAll { a: Int, b: Int ->
                a <= 0 || b <= 0 || add(a, b) == a + b
            }
        }
    }
}
