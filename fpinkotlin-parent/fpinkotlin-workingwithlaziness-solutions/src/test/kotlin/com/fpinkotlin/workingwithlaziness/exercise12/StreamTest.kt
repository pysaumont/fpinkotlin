package com.fpinkotlin.workingwithlaziness.exercise12

import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class StreamTest: StringSpec() {

    init {

        "head&tail" {
            forAll { a: Int ->
                val stream = Stream.from(a)
                val first = stream.head().getOrElse(0)
                val second = stream.tail().getOrElse(Stream.invoke()).head().getOrElse(0)
                first == a && second == a + 1
            }
        }
    }
}