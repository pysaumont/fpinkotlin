package com.fpinkotlin.workingwithlaziness.exercise26

import com.fpinkotlin.common.range
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class StreamTest: StringSpec() {

    init {

        "flatMap" {
            forAll(10, Gen.choose(0, 1_000)) { a ->
                fun inc(i: Int) = i + 1
                val list = range(0, a)
                val stream = Stream.iterate(Lazy{ inc(-1) }, ::inc).takeWhileViaFoldRight { it < a }.flatMap { Stream.from(it).takeAtMost(1) }
                list.toString() ==  stream.toList().toString()
            }
        }
    }

}
