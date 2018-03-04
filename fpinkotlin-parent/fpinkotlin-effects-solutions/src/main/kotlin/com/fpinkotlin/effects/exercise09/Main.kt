package com.fpinkotlin.effects.exercise09

import com.fpinkotlin.common.Lazy
import com.fpinkotlin.effects.exercise09.IO.Companion.condition


private val buildMessage = { name: String ->
    condition(name.isNotEmpty(), Lazy {
        IO("Hello, $name!").flatMap { Console.println(it) }
    })
}

fun program(f: (String) -> IO<Boolean>, title: String): IO<Unit> {
    return IO.sequence(Console.println(title),
            IO.doWhile(Console.readln(), f),
            Console.println("bye!")
    )
}

fun main(args: Array<String>) {

    val program = program(buildMessage, "Enter the names of the persons to welcome: ")
    program()
}
