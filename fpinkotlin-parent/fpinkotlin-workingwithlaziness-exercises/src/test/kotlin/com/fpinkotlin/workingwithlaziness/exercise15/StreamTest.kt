package com.fpinkotlin.workingwithlaziness.exercise15

import com.fpinkotlin.common.range
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class StreamTest: StringSpec() {

    init {

        "toList" {
            forAll(10, Gen.choose(0, 1_000)) { a ->
                val list = range(0, a)
                val stream = Stream.from(0).takeAtMost(a)
                list.toString() == stream.toList().toString()
            }
        }
    }
}
