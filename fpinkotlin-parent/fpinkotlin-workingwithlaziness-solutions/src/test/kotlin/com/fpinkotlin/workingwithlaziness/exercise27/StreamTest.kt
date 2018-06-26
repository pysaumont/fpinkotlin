package com.fpinkotlin.workingwithlaziness.exercise27

import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class LazyTest: StringSpec() {

    init {

        "find" {
            forAll(IntGenerator(0, 1_000), { a ->
                val stream = Stream.from(0).takeAtMost(a)
                stream.find { it == a / 2 }.getOrElse(0) == a / 2 &&
                        stream.find { it == a * 2 }.getOrElse(-1) == -1
            })
        }
    }
}
