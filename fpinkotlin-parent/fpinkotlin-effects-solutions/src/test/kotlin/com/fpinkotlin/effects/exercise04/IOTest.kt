package com.fpinkotlin.effects.exercise04

import com.fpinkotlin.common.List
import io.kotlintest.specs.StringSpec

class IOTest: StringSpec() {

    init {

        "add" {
            val name = getName()

            // These three lines do nothing. each line returns a program with a
            val instruction1 = show("Hello, ")
            val instruction2 = show(name)
            val instruction3 = show("!\n")

            // Write a script
            val script = instruction1.add(instruction2).add(instruction3)
            // execute it
            script()

            // Or simpler:
            show("Hello, ").add(show(name)).add(show("!\n"))()

            // We can make a script from a list of instructions:

            val script2 = List(
                    show("Hello, "), // Doesn't this look like an imperative program?
                    show(name),
                    show("!\n")
            )
            // We can sort of "compile" it, then execute it
            script2.foldRight(IO.empty, { io -> { io.add(it) } })()
            script2.foldLeft(IO.empty, { acc -> { acc.add(it) } })()
        }

    }

    private fun show(message: String): IO = object : IO {
        override fun invoke() { println(message) }
    }

    private fun getName(): String = "Mickey"

}
