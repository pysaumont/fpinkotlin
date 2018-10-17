package com.fpinkotlin.workingwithlaziness.exercise25

import com.fpinkotlin.common.range
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class StreamTest: StringSpec() {

    init {

        "append" {
            forAll(10, Gen.choose(0, 1_000), Gen.choose(0, 500)) { a, limit ->
                fun inc(i: Int): Int = i + 1
                val stream = Stream
                        .iterate(Lazy{ inc(0) }, ::inc)
                        .takeAtMost(a)
                        .append(Lazy { Stream.iterate(Lazy{ inc(a) }, ::inc) })
                        .filter { it % 2 != 0 }
                        .takeWhileViaFoldRight { it < limit * 2 }
                val result = stream.toList()
                val list = range(0, limit * 2).filter { it % 2 != 0 }
                result.toString() == list.toString()
            }
        }
    }

}
