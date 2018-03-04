package com.fpinkotlin.effects.exercise04

import com.fpinkotlin.common.List
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class IOTest: StringSpec() {

    var result = ""

    init {

        "add" {

            // These three lines do nothing. each line returns a program with a
            val instruction0 = reset()
            val instruction1 = append("Hello, ")
            val instruction2 = append(getName())
            val instruction3 = append("!")

            // Write a script
            val script = instruction0 + instruction1 + instruction2 + instruction3

            // execute it
            script()
            result.shouldBe("Hello, Mickey!")

            // Or:
            (reset() + append("Hello, ") + append("Donald") + append("!"))()
            result.shouldBe("Hello, Donald!")

            // We can make a script from a list of instructions:
            val script2 = List(
                    append("Hello, "), // Doesn't this look like an imperative program?
                    append(getName()),
                    append("!")
            )

            // We can sort of "compile" it, then execute it using a left fold:
            script2.foldLeft(reset(), { acc -> { acc + it } })()
            result.shouldBe("Hello, Mickey!")

            // We can also use a right fold but beware that with foldRight,
            // the identity will be applied last. So using reset() as the identity
            // would result into an empty string
            reset()()
            script2.foldRight(IO.empty, { io -> { io + it } })()
            result.shouldBe("Hello, Mickey!")
        }

    }

    private fun reset(): IO = IO { result = "" }

    private fun append(message: String): IO = IO { result += message }

    private fun getName(): String = "Mickey"

}
