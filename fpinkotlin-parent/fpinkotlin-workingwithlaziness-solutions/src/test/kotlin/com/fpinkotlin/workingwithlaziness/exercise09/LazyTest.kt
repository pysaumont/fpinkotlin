package com.fpinkotlin.workingwithlaziness.exercise09

import com.fpinkotlin.common.List
import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class LazyTest: StringSpec() {

    init {

        "sequenceResult" {
            forAll(IntGenerator(), { a ->
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
                var name4Calls = 0
                val name4 = Lazy {
                    name4Calls++
                    throw IllegalStateException("Exception while evaluating name4")
                }

                val list = sequenceResult(List(name1, name2, name3, name4))
                val defaultMessage = "No greetings when time is odd"
                val condition = a % 2 == 0
                val result1 = if (condition) list().toString() else defaultMessage
                val result2 = if (condition) list().toString() else defaultMessage
                (!condition && result1 == "No greetings when time is odd" &&
                        result1 == result2 &&
                        name1Calls == 0 &&
                        name2Calls == 0 &&
                        name3Calls == 0 &&
                        name4Calls == 0) ||
                        (condition &&
                                result1 == result2 &&
                                name1Calls == 1 &&
                                name2Calls == 1 &&
                                name3Calls == 1 &&
                                name4Calls == 1)
            })
        }

        "sequenceResult3" {
            forAll(IntGenerator(), { a ->
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
                var name4Calls = 0
                val name4 = Lazy {
                    name4Calls++
                    throw IllegalStateException("Exception while evaluating name4")
                }

                val list = sequenceResult3(List(name1, name2, name3, name4))
                val defaultMessage = "No greetings when time is odd"
                val condition = a % 2 == 0
                val result1 = if (condition) list().toString() else defaultMessage
                val result2 = if (condition) list().toString() else defaultMessage
                (!condition && result1 == "No greetings when time is odd" &&
                        result1 == result2 &&
                        name1Calls == 0 &&
                        name2Calls == 0 &&
                        name3Calls == 0 &&
                        name4Calls == 0) ||
                        (condition &&
                                result1 == result2 &&
                                name1Calls == 1 &&
                                name2Calls == 1 &&
                                name3Calls == 1 &&
                                name4Calls == 1)
            })
        }
    }
}