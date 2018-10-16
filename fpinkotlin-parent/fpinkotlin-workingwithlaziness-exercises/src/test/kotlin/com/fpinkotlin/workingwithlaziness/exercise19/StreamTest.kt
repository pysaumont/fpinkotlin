package com.fpinkotlin.workingwithlaziness.exercise19

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class StreamTest: StringSpec() {

    init {

        "exists" {
            forAll(10, Gen.choose(0, 1_000)) { a ->
                var incCalls = 0
                fun inc(i: Int): Int {
                    incCalls++
                    return i + 1
                }
                val start = a / 2
                val stream = Stream.iterate(Lazy{ inc(-1) }, ::inc).dropWhile { it < start }.takeWhile { it < a }
                val testValue = a - start / 2
                val result1 = stream.exists { it == testValue }
                val evaluated = incCalls
                val result2 = stream.exists { it == a }
                result1 &&
                        !result2 &&
                        evaluated == testValue + 1 && // all drop values + 1 for seed
                        incCalls == a + 1 // + 1 for seed
            }
        }
    }
}
