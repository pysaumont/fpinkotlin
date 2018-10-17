package com.fpinkotlin.workingwithlaziness.exercise23

import com.fpinkotlin.common.range
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class StreamTest: StringSpec() {

    init {

        "map" {
            forAll(10, Gen.choose(0, 1_000)) { a ->
                var incCalls = 0
                fun inc(i: Int): Int {
                    incCalls++
                    return i + 1
                }
                val list = range(0, a).map { it * 2 }
                val stream = Stream.iterate(Lazy{ inc(-1) }, ::inc).takeWhileViaFoldRight { it < a }.map { it * 2 }
                val evaluated = incCalls
                val result1 = stream.toList().toString()
                val result2 = stream.toList().toString()
                list.toString() == result1 && result2 == result1 && evaluated == 1 && incCalls == a + 1 // + 1 for seed
            }
        }
    }

}
