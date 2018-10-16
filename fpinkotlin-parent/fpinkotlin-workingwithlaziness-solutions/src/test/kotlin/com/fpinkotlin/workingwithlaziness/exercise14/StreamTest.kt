package com.fpinkotlin.workingwithlaziness.exercise14

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class StreamTest: StringSpec() {

    init {

        "dropAtMost&takeAtMost" {
            forAll(Gen.choose(0, 100_000), Gen.choose(0, 500)) { a, offset ->
                val stream = Stream.from(a).dropAtMost(offset).takeAtMost(offset)
                stream.isEmpty() || stream.head().map { it == a + offset }.getOrElse(false)
            }
        }
    }
}
