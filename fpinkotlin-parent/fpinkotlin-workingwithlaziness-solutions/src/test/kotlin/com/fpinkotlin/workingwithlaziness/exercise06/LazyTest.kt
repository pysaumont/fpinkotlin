package com.fpinkotlin.workingwithlaziness.exercise06

import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class LazyTest: StringSpec() {

    init {

        "Lazy" {
            forAll(IntGenerator(), { a ->
                val greets: (String) -> String = { "Hello, $it!" }

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
                val message = name.map(greets)
                val condition = a % 2 == 0
                val result1 = if (condition) message() else defaultMessage()
                val result2 = if (condition) message() else defaultMessage()
                (!condition && result1 == "No greetings when time is odd" &&
                        result2 == result1 &&
                        nameCalls == 0 &&
                        defaultMessageCalls == 1) ||
                        (condition &&
                                result1 == "Hello, Mickey!" &&
                                result2 == result1 &&
                                nameCalls == 1 &&
                                defaultMessageCalls == 0)
            })
        }
    }
}