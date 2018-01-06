package com.fpinkotlin.workingwithlaziness.exercise20

import com.fpinkotlin.common.range
import com.fpinkotlin.common.sum
import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class LazyTest: StringSpec() {

    init {

        "foldRight" {
            forAll(IntGenerator(0, 10_000), { a ->
                var incCalls = 0
                fun inc(i: Int): Int {
                    incCalls++
                    return i + 1
                }
                val stream = Stream.iterate(a, ::inc).takeAtMost(100)
                val expected = sum(range(a , a + 100))
                val result = stream.foldRight(Lazy { 0 }) { x -> { y -> x + y() } }
                expected == result
            }, 10)
        }
    }
}
