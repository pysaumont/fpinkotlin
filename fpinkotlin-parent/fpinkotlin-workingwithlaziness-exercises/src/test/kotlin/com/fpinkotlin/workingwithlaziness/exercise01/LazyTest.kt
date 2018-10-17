package com.fpinkotlin.workingwithlaziness.exercise01

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class LazyTest: StringSpec() {

    init {

        "Lazy" {
            forAll(Gen.choose(0, 10)) { i: Int ->

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
                (0..i).forEach { _ -> first() || second() }
                i == 0 || (1 == firstCalls) && (0 == secondCalls)
            }
        }
    }
}