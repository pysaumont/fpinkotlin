package com.fpinkotlin.recursion.exercise02

import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import com.fpinkotlin.recursion.exercise02.Factorial.factorial
import io.kotlintest.specs.StringSpec

class FactorialTest: StringSpec() {

    init {

        "factorial" {
            forAll(IntGenerator(1, 30), { n ->
                factorial(n + 1) == factorial(n) * (n + 1)
            })
        }
    }
}
