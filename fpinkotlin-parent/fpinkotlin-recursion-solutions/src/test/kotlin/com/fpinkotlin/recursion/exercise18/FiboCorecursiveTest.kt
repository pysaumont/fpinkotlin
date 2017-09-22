package com.fpinkotlin.recursion.exercise18

import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec


class FiboCorecursiveTest : StringSpec() {

    init {

        "fibonacci" {
            forAll(IntGenerator(3, 300), { n ->
                fiboCorecursive(n - 1) == fiboCorecursive(n).substringBeforeLast(",")
            }, 100)
        }
    }
}