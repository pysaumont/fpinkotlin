package com.fpinkotlin.workingwithlaziness.exercise26

import com.fpinkotlin.common.range
import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class LazyTest: StringSpec() {

    init {

        "flatMap" {
            forAll(IntGenerator(0, 1_000), { a ->
                fun inc(i: Int) = i + 1
                val list = range(0, a)
                val stream = Stream.iterate(Lazy{ inc(-1) }, ::inc).takeWhileViaFoldRight { it < a }.flatMap { Stream.from(it).takeAtMost(1) }
                list.toString() ==  stream.toList().toString()
            }, 10)
        }
    }

}
