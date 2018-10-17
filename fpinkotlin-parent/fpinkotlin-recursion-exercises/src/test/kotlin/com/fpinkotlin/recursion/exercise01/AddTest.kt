package com.fpinkotlin.recursion.exercise01

import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "add" {
            forAll { a: Int, b: Int ->
                add(a, b) == a + b
            }
        }
    }
}
