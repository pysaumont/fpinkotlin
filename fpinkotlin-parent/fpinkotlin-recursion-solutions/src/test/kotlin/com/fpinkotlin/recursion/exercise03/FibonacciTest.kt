package com.fpinkotlin.recursion.exercise03

import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class FibonacciTest : StringSpec() {

    init {

        "fibonacci" {
            forAll(IntGenerator(3, 30_000), { n ->
                fib(n) == fib(n - 1) + fib (n - 2)
            }, 100)
        }
    }
}