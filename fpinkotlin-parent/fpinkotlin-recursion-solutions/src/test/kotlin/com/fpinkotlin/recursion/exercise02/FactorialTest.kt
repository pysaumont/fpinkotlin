package com.fpinkotlin.recursion.exercise02

import com.fpinkotlin.recursion.exercise02.Factorial.factorial
import com.fpinkotlin.recursion.exercise02.Factorial2.factorial2
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class FactorialTest: StringSpec() {

    init {

        "factorial" {
            forAll(Gen.choose(1, 30)) { n ->
                factorial(n + 1) == factorial(n) * (n + 1)
            }
        }
        "factorial2" {
            forAll(Gen.choose(1, 30)) { n ->
                factorial2(n + 1) == factorial2(n) * (n + 1)
            }
        }
    }
}
