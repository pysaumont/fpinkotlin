package com.fpinkotlin.workingwithlaziness.exercise16

import com.fpinkotlin.common.range
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class StreamTest: StringSpec() {

    init {

        "iterate" {
            forAll(10, Gen.choose(0, 1_000)) { a ->
                val list = range(0, a)
                val stream = Stream.iterate(0) { it + 1 }.takeAtMost(a)
                list.toString() == stream.toList().toString()
            }
        }
    }
}
