package com.fpinkotlin.workingwithlaziness.exercise29

import com.fpinkotlin.common.Result
import com.fpinkotlin.common.range
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class StreamTest: StringSpec() {

    init {

        "unfold" {
            forAll(10, Gen.choose(0, 1_000)) { a ->
                val list = range(0, a)
                val stream = Stream.unfold(-1) { Result(Pair(it + 1, it + 1)) }.takeAtMost(a)
                list.toString() == stream.toList().toString()
            }
        }
    }
}
