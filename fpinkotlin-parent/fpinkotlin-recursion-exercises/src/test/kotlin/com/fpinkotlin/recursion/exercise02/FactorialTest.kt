package com.fpinkotlin.recursion.exercise02

import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class FactorialTest: StringSpec() {

    init {

        "factorial" {
            forAll(IntGenerator(1, 30), { n ->
                Factorial.factorial(n + 1) == Factorial.factorial(n) * (n + 1)
            })
        }
    }
}
