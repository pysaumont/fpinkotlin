package com.fpinkotlin.workingwithlaziness.exercise27

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class StreamTest: StringSpec() {

    init {

        "find" {
            forAll(Gen.choose(0, 1_000)) { a ->
                val stream = Stream.from(0).takeAtMost(a)
                stream.find { it == a / 2 }.getOrElse(0) == a / 2 &&
                        stream.find { it == a * 2 }.getOrElse(-1) == -1
            }
        }
    }
}
