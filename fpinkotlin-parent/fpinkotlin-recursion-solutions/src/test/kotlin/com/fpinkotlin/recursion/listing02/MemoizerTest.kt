package com.fpinkotlin.recursion.listing02


import com.fpinkotlin.recursion.exercise03.fib
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec


class FiboCorecursiveTest : StringSpec() {

    init {

        "fibonacci" {
            forAll(100, Gen.choose(3, 300)) { n ->
                fib(n) == fib(n - 1) + fib (n - 2)
            }
        }
    }
}