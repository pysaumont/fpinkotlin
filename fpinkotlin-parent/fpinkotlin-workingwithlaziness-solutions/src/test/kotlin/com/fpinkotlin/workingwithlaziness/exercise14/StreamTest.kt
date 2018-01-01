package com.fpinkotlin.workingwithlaziness.exercise14

import com.fpinkotlin.common.range
import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class LazyTest: StringSpec() {

    init {

        "toList" {
            forAll(IntGenerator(0, 10_000), { a ->
                val list = range(0, a)
                val stream = Stream.from(0).takeAtMost(a)
                list.toString() == stream.toList().toString()
            }, 10)
        }
    }
}
