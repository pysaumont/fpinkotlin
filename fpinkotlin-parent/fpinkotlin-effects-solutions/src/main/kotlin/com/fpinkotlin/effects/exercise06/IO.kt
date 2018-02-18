package com.fpinkotlin.effects.exercise06


class IO<out A>(private val f: () -> A) { // <1>

    operator fun invoke() = f()

    fun <B> map (g: (A) -> B): IO<B> = IO {
        g(this())
    }

    companion object {

        val empty: IO<Unit> = IO { } // <2>

        operator fun <A> invoke(a: A): IO<A> = IO { a } // <3>
    }
}

fun main(args: Array<String>) {
    val script = sayHello()
    script()
}

private fun sayHello(): IO<Unit> = Console.print("Enter your name: ")
        .map { Console.readln()() }
        .map { buildMessage(it) }
        .map { Console.println(it)() }

private fun buildMessage(name: String): String = "Hello, $name!"
