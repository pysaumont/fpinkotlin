package com.fpinkotlin.recursion.listing02


import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import com.fpinkotlin.recursion.exercise03.fib
import io.kotlintest.specs.StringSpec


class FiboCorecursiveTest : StringSpec() {

    init {

        "fibonacci" {
            forAll(IntGenerator(3, 300), { n ->
                //val f = Memoizer.memoize<Int, String> { n -> fiboCorecursive(n) }
                fib(n) == fib(n - 1) + fib (n - 2)
            }, 100)
        }
    }
}