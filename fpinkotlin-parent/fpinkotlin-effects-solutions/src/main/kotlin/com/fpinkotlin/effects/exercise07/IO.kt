package com.fpinkotlin.effects.exercise07


class IO<out A>(private val f: () -> A) {

    operator fun invoke() = f()

    fun <B> map (g: (A) -> B): IO<B> = IO {
        g(this())
    }

    fun <B> flatMap (g: (A) -> IO<B>): IO<B> = IO {
        g(this())()
    }

    companion object {

        val empty: IO<Unit> = IO { }

        operator fun <A> invoke(a: A): IO<A> = IO { a }
    }
}

fun main(args: Array<String>) {
    val script = sayHello()
    script()
}

private fun sayHello(): IO<Unit> = Console.print("Enter your name: ")
        .flatMap { Console.readln() }
        .map { buildMessage(it) }
        .flatMap { Console.println(it) }

private fun buildMessage(name: String): String = "Hello, $name!"
