package com.fpinkotlin.workingwithlaziness.exercise11

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class StreamTest: StringSpec() {

    var n = 0
    fun next() = ++n

    init {

        "repeat" {
            forAll(Gen.choose(0, 100_000)) { a ->
                tailrec fun drop(a: Int, s: Stream<Int>): Stream<Int> = when (a) {
                    0 -> s
                    else -> drop( a - 1, s.tail().getOrElse { throw RuntimeException("No tail") })
                }
                val x = a % 10000 // limit the
                val stream = drop(x, Stream.repeat{ next() })
                val result = stream.head().getOrElse(-1)
                n = 0
                result == 1
            }
        }
    }
}