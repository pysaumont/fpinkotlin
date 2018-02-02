package com.fpinkotlin.workingwithlaziness.exercise04

import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import com.fpinkotlin.workingwithlaziness.exercise04.Lazy.Companion.lift2
import io.kotlintest.specs.StringSpec

class LazyTest: StringSpec() {

    init {

        "Lazy" {
            forAll(IntGenerator(), { a ->
                var greetingsCalls = 0
                val greetings = Lazy {
                    greetingsCalls++
                    "Hello"
                }
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
                var defaultMessageCalls = 0
                val defaultMessage = Lazy {
                    defaultMessageCalls++
                    "No greetings when time is odd"
                }
                val greetingString = lift2(consMessage)(greetings)
                val message1 = greetingString(name1)
                val message2 = greetingString(name2)
                val condition = a % 2 == 0
                val result1 = if (condition) message1() else defaultMessage()
                val result2 = if (condition) message1() else defaultMessage()
                val result3 = if (condition) message2() else defaultMessage()
                (!condition && result1 == "No greetings when time is odd" &&
                        result2 == result1 &&
                        result3 == result1 &&
                        greetingsCalls == 0 &&
                        name1Calls == 0 &&
                        name2Calls == 0 &&
                        defaultMessageCalls == 1) ||
                        (condition &&
                                result1 == "Hello, Mickey!" &&
                                result2 == result1 &&
                                result3 == "Hello, Donald!" &&
                                greetingsCalls == 1 &&
                                name1Calls == 1 &&
                                name2Calls == 1 &&
                                defaultMessageCalls == 0)
            })

        }
    }
}