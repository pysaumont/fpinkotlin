package com.fpinkotlin.workingwithlaziness.exercise11

import com.fpinkotlin.common.getOrElse
import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class LazyTest: StringSpec() {

    init {

        "head&tail" {
            forAll(IntGenerator(), { a ->
                val stream = Stream.from(a)
                val first = stream.head().getOrElse(0)
                val second = stream.tail().getOrElse(Stream.invoke()).head().getOrElse(0)
                first == a && second == a + 1
            })
        }
    }
}