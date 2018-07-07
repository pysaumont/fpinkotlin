package com.fpinkotlin.effects.exercise04

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result


class IO(private val f: () -> Unit) {

    operator fun invoke() = f()

    operator fun plus(io: IO): IO = IO {
            f()
            io.f()
        }

    companion object {
        val empty: IO = IO {}
    }
}

fun show(message: String): IO = IO { println(message) }

fun <A> toString(rd: Result<A>): String =
        rd.map { it.toString() }.getOrElse { rd.toString() }


fun inverse(i: Int): Result<Double> = when (i) {
    0 -> Result.failure("Div by 0")
    else -> Result(1.0 / i)
}

fun main(args: Array<String>) {
    val computation: IO = show(toString(inverse(3)))
    computation()
    val error = show(toString(inverse(0)))
    error()


    fun getName() = "Mickey"

    val instruction1 = IO { print("Hello, ") }
    val instruction2 = IO { print(getName()) }
    val instruction3 = IO { print("!\n") }

    val script: IO = instruction1 + instruction2 + instruction3
    script()
    instruction1.plus(instruction2).plus(instruction3)()

    val script2 = List(
            IO { print("Hello, ") },
            IO { print(getName()) },
            IO { print("!\n") }
    )
    val program: IO = script2.foldRight(IO.empty) { io -> { io + it } }

}