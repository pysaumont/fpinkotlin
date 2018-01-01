package com.fpinkotlin.workingwithlaziness.exercise29

import com.fpinkotlin.common.Result
import com.fpinkotlin.common.range
import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class LazyTest: StringSpec() {

    init {

        "unfold" {
            forAll(IntGenerator(0, 10_000), { a ->
                val list = range(0, a)
                val stream = Stream.unfold(-1) { Result(Pair(it + 1, it + 1)) }.takeAtMost(a)
                list.toString() == stream.toList().toString()
            }, 10)
        }
    }
}
