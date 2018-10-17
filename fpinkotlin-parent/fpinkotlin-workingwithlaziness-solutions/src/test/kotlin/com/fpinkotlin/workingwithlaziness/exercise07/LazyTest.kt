package com.fpinkotlin.workingwithlaziness.exercise07

import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class LazyTest: StringSpec() {

    init {

        "Lazy" {
            forAll { a: Int ->
                var greetingsCalls = 0
                val greetings = Lazy {
                    greetingsCalls++
                    "Hello"
                }
                val flatGreets: (String) -> Lazy<String> = { name -> greetings.map { "$it, $name!"} }

                var nameCalls = 0
                val name: Lazy<String> = Lazy {
                    nameCalls++
                    "Mickey"
                }
                var defaultMessageCalls = 0
                val defaultMessage = Lazy {
                    defaultMessageCalls++
                    "No greetings when time is odd"
                }
                val message = name.flatMap(flatGreets)
                val condition = a % 2 == 0
                val result1 = if (condition) message() else defaultMessage()
                val result2 = if (condition) message() else defaultMessage()
                (!condition && result1 == "No greetings when time is odd" &&
                        result2 == result1 &&
                        greetingsCalls == 0 &&
                        nameCalls == 0 &&
                        defaultMessageCalls == 1) ||
                        (condition &&
                                result1 == "Hello, Mickey!" &&
                                result2 == result1 &&
                                greetingsCalls == 1 &&
                                nameCalls == 1 &&
                                defaultMessageCalls == 0)
            }
        }
    }
}