package com.fpinkotlin.workingwithlaziness.exercise18

import com.fpinkotlin.common.range
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class StreamTest: StringSpec() {

    init {

        "dropWhile" {
            forAll(10, Gen.choose(0, 1_000)) { a ->
                var incCalls = 0
                fun inc(i: Int): Int {
                    incCalls++
                    return i + 1
                }
                val start = a / 2
                val list = range(start, a)
                val stream = Stream.iterate(Lazy{ inc(-1) }, ::inc).dropWhile { it < start }.takeWhile { it < a }
                val evaluated = incCalls
                val result1 = stream.toList().toString()
                val result2 = stream.toList().toString()
                list.toString() == result1 &&
                        result2 == result1 &&
                        evaluated == start + 1 && // all drop values + 1 for seed
                        incCalls == a + 1 // + 1 for seed
            }
        }
    }

}
