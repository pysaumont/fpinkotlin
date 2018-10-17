package com.fpinkotlin.workingwithlaziness.exercise08

import com.fpinkotlin.common.List
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class LazyTest: StringSpec() {

    init {

        "Lazy" {
            forAll { a: Int ->
                var name1Calls = 0
                val name1: Lazy<String> = Lazy {
                    name1Calls++
                    "Mickey"
                }
                var name2Calls = 0
                val name2: Lazy<String> = Lazy {
                    name2Calls++
                    "Donald"
                }
                var name3Calls = 0
                val name3: Lazy<String> = Lazy {
                    name3Calls++
                    "Goofy"
                }
                val list = sequence(List(name1, name2, name3))
                val defaultMessage = "No greetings when time is odd"
                val condition = a % 2 == 0
                val result1 = if (condition) list().toString() else defaultMessage
                val result2 = if (condition) list().toString() else defaultMessage
                (!condition && result1 == "No greetings when time is odd" &&
                        result1 == result2 &&
                        name1Calls == 0 &&
                        name2Calls == 0 &&
                        name3Calls == 0) ||
                (condition &&
                        result1 == result2 &&
                        name1Calls == 1 &&
                        name2Calls == 1 &&
                        name3Calls == 1)
            }
        }
    }
}