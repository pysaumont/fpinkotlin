package com.fpinkotlin.effects.exercise05


class IO<out A>(private val f: () -> A) {

    operator fun invoke() = f()

    companion object {

        val empty: IO<Unit> = IO { }

        operator fun <A> invoke(a: A): IO<A> = IO { a }
    }
}

fun main(args: Array<String>) {
    Console.print("Enter your name: ")()
    Console.println("Hello, ${Console.readln()()}")()
}

