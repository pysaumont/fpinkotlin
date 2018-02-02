package com.fpinkotlin.recursion.listing04_01


import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class MemoizedFibonacciTest : StringSpec() {

    init {

        "fibonacci" {
            forAll(IntGenerator(3, 300), { n ->
                fibo(n - 1) == fibo(n).substringBeforeLast(",")
            }, 100)
        }
    }
}