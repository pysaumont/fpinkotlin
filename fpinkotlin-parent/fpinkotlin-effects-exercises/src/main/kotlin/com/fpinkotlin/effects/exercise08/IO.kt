package com.fpinkotlin.effects.exercise08

import com.fpinkotlin.common.List


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

        fun <A> repeat(n: Int, io: IO<A> ): IO<List<A>> = TODO("repeat")

        fun <A, B, C> map2(ioa: IO<A>, iob: IO<B>, f: (A) ->  (B) -> C): IO<C> = TODO("map2")
    }
}

fun main(args: Array<String>) {
    val program = IO.repeat(3, sayHello())
    program()
}

private fun sayHello(): IO<Unit> = Console.print("Enter your name: ")
        .flatMap { Console.readln() }
        .map { buildMessage(it) }
        .flatMap { Console.println(it) }

private fun buildMessage(name: String): String = "Hello, $name!"
