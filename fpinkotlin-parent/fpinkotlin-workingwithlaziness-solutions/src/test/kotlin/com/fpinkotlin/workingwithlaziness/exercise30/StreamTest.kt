package com.fpinkotlin.workingwithlaziness.exercise30

import com.fpinkotlin.common.range
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class StreamTest: StringSpec() {

    init {

        "filter" {
            forAll(50, Gen.choose(0, 5_000)) { a ->
                fun inc(i: Int): Int {
                    return i + 1
                }
                val list = range(a / 2 + 1, a).filter { it % 2 == 0 }
                val stream = Stream.iterate(Lazy{ inc(-1) }, ::inc).takeWhileViaFoldRight { it < a }.filter { it > a / 2 && it % 2 == 0 }
                list.toString() == stream.toList().toString()
            }
        }
    }
}
