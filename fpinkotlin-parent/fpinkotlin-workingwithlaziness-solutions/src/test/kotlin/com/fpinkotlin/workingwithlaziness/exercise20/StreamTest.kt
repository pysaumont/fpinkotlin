package com.fpinkotlin.workingwithlaziness.exercise20

import com.fpinkotlin.common.range
import com.fpinkotlin.common.sum
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class StreamTest: StringSpec() {

    init {

        "foldRight" {
            forAll(10, Gen.choose(0, 1_000)) { a ->
                var incCalls = 0
                fun inc(i: Int): Int {
                    incCalls++
                    return i + 1
                }
                val stream = Stream.iterate(a, ::inc).takeAtMost(100)
                val expected = range(a , a + 100).sum()
                val result = stream.foldRight(Lazy { 0 }) { x -> { y -> x + y() } }
                expected == result
            }
        }
    }
}
