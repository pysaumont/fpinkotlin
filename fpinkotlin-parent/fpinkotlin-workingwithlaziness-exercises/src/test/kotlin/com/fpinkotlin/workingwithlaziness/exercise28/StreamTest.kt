package com.fpinkotlin.workingwithlaziness.exercise28

import com.fpinkotlin.common.getOrElse
import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class LazyTest: StringSpec() {

    init {

        "fibs" {
            forAll(IntGenerator(3, 50), { a ->
                val stream = fibs()
                val r1 = stream.dropAtMost(a - 1).head()
                val r2 = stream.dropAtMost(a - 2).head()
                val r3 = stream.dropAtMost(a - 3).head()
                r1.flatMap { x -> r2.flatMap { y -> r3.map { z -> z + y == x } } }.getOrElse(false)
            })
        }
    }

}
