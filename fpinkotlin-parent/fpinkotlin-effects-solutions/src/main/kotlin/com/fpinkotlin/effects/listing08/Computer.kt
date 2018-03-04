package com.fpinkotlin.effects.listing08

import com.fpinkotlin.common.Stream
import com.fpinkotlin.effects.listing08.Console.printLine
import com.fpinkotlin.effects.listing08.Console.readLine
import com.fpinkotlin.effects.listing08.IO.Companion.condition
import com.fpinkotlin.effects.listing08.IO.Companion.doWhile
import com.fpinkotlin.effects.listing08.IO.Companion.forEach
import com.fpinkotlin.effects.listing08.IO.Companion.ref
import com.fpinkotlin.effects.listing08.IO.Companion.sequence
import com.fpinkotlin.effects.listing08.IO.Companion.skip
import com.fpinkotlin.effects.listing08.IO.Companion.unit
import java.math.BigInteger


internal object Computer {

    private val factorialComputer: (String) -> IO<Boolean> = { line ->
        condition(line != "x") {
            factorial(BigInteger.valueOf(line.toLong()))
                .map { "factorial: $it" }
                .flatMap(Console::printLine)
        }
    }

    private val squareComputer: (String) -> IO<Boolean> = { line ->
        condition(line != "x") {
            unit(square(BigInteger.valueOf(line.toLong())))
                .map { "square: $it" }
                .flatMap(Console::printLine)
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {

//        factorial(5).flatMap(Console::printline)()

        compute(factorialComputer, "factorial")()
//        compute(squareComputer, "square")()
    }


    fun readInt(): IO<String> = Console.readLine()

    fun factorial(n: Int): IO<Int> =
        ref(1).flatMap { acc ->
            forEach(Stream.range(1, n)) { skip(acc.modify { x -> x * it }) }
                .flatMap { acc.get() }
        }

    fun factorial(n: BigInteger): IO<BigInteger> =
        ref(BigInteger.ONE).flatMap { acc ->
                     forEach(Stream.range(BigInteger.ONE, n)) { skip(acc.modify { x -> x * it }) }
                         .flatMap { acc.get() }
                 }

    fun square(n: BigInteger): BigInteger = n * n

    private fun compute(f: (String) -> IO<Boolean>, name: String): IO<Unit> = sequence(
        printLine("The $name computer"),
        printLine(" - Enter a number to compute its $name"),
        printLine(" - Enter 'x' to exit"),
        doWhile(readLine(), f),
        printLine("I'll be back."))
}

