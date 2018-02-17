package com.fpinkotlin.effects.exercise04

import com.fpinkotlin.common.Result
import com.fpinkotlin.common.getOrElse



interface IO {

    operator fun invoke()

    fun add(io: IO): IO = object : IO {
        override fun invoke() {
            this()
            io()
        }
    }

    companion object {
        val empty: IO = object : IO {
            override fun invoke() {}
        }
    }
}

fun show(message: String): IO = object : IO {
    override fun invoke() { println(message) }
}

fun <A> toString(rd: Result<A>): String {
    return rd.map { it.toString() }.getOrElse { rd.toString() }
}

fun inverse(i: Int): Result<Double> = when (i) {
    0 -> Result.failure("Div by 0")
    else -> Result(1.0 / i)
}

fun main(args: Array<String>) {
    val computation: IO = show(toString(inverse(3)))
    computation()
}