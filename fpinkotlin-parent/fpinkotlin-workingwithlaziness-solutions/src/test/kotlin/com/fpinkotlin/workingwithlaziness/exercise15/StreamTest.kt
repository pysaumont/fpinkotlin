package com.fpinkotlin.workingwithlaziness.exercise15

import com.fpinkotlin.common.range
import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class LazyTest: StringSpec() {

    init {

        "iterate" {
            forAll(IntGenerator(0, 10_000), { a ->
                val list = range(0, a)
                val stream = Stream.iterate(0) { it + 1}.takeAtMost(a)
                list.toString() == stream.toList().toString()
            }, 10)
        }
    }
}
