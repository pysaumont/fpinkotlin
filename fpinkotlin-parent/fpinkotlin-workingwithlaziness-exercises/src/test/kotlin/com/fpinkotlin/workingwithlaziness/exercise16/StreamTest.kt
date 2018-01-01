package com.fpinkotlin.workingwithlaziness.exercise16

import com.fpinkotlin.common.range
import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class LazyTest: StringSpec() {

    init {

        "iterate" {
            forAll(IntGenerator(0, 10_000), { a ->
                var incCalls = 0
                fun inc(i: Int): Int {
                    incCalls++
                    return i + 1
                }
                val list = range(0, a)
                val stream = Stream.iterate(Lazy{ inc(-1) }, ::inc).takeAtMost(a)
                val evaluated = incCalls
                val result1 = stream.toList().toString()
                val result2 = stream.toList().toString()
                list.toString() == result1 && result2 == result1 && evaluated == 0 && incCalls == a + 1 // + 1 for seed
            }, 10)
        }
    }

}
