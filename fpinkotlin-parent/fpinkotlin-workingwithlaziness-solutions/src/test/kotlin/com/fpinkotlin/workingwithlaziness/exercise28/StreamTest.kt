package com.fpinkotlin.workingwithlaziness.exercise28

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class StreamTest: StringSpec() {

    init {

        "fibs" {
            forAll(Gen.choose(3, 50)) { a ->
                val stream = fibs()
                val r1 = stream.dropAtMost(a - 1).head()
                val r2 = stream.dropAtMost(a - 2).head()
                val r3 = stream.dropAtMost(a - 3).head()
                r1.flatMap { x -> r2.flatMap { y -> r3.map { z -> z + y == x } } }.getOrElse(false)
            }
        }
    }

}
