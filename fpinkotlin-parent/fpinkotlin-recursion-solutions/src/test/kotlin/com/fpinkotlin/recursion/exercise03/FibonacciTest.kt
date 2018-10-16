package com.fpinkotlin.recursion.exercise03

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class FibonacciTest : StringSpec() {

    init {

        "fibonacci" {
            forAll(100, Gen.choose(3, 30_000)) { n ->
                fib(n) == fib(n - 1) + fib (n - 2)
            }
        }
    }
}