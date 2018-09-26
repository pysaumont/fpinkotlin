package com.fpinkotlin.workingwithlaziness.exercise01

import com.fpinkotlin.generators.IntGenerator
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class LazyTest: StringSpec() {

    init {

        "Lazy" {
            forAll(IntGenerator()) {

                var firstCalls = 0
                var secondCalls = 0
                val first = Lazy {
                    firstCalls++
                    true
                }
                val second = Lazy {
                    secondCalls++
                    throw IllegalStateException()
                }
                (first() || second()) &&
                        (first() || second()) &&
                        (1 == firstCalls) &&
                        (0 == secondCalls)
            }
        }
    }
}