package com.fpinkotlin.workingwithlaziness.exercise11

import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class StreamTest: StringSpec() {

    var n = 0
    fun next() = ++n

    init {

        "repeat" {
            forAll(IntGenerator(0, 100_000), { a ->
                tailrec fun drop(a: Int, s: Stream<Int>): Stream<Int> = when (a) {
                    0 -> s
                    else -> drop( a - 1, s.tail().getOrElse { throw RuntimeException("No tail") })
                }
                val x = a % 10000 // limit the
                val stream = drop(x, Stream.repeat(this::next))
                val result = stream.head().getOrElse(-1)
                n = 0
                /*
                 * If you wonder why result should be equal to 0, it is because dropped values from
                 * the stream (the x first values) should not have been evaluated, so the next() function
                 * should have been called only once for each test.
                 */
                result == 1
            })
        }
    }
}