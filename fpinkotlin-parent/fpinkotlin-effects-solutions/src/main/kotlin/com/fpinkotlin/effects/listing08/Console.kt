package com.fpinkotlin.effects.listing08

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


object Console {

    private val br = BufferedReader(InputStreamReader(System.`in`))

    /**
     * A possible implementation of readLine as a val function
     */
    val readLine2: () -> IO<String> = {
        IO.Suspend {
            try {
                br.readLine()
            } catch (e: IOException) {
                throw IllegalStateException(e)
            }
        }
    }

    /**
     * A simpler implementation of readLine. A function reference is not possible
     * due to the name clash. Use different names for fun and val function if you
     * want to use function references
     */
    val readLine = { readLine() }

    /**
     * A fun version of readLine
     */
    fun readLine(): IO<String> = IO.Suspend {
            try {
                br.readLine()
            } catch (e: IOException) {
                throw IllegalStateException(e)
            }
        }

    /**
     * A vall version of printLine
     */
    internal val printLine: (String) -> IO<Unit> = { s: Any ->
        IO.Suspend {
            println(s)
        }
    }

    /**
     * A fun version of printLine
     */
    fun printLine(s: Any): IO<Unit> = IO.Suspend { println(s) }

    /**
     * A print function. Note the fully qualified call to kotlin.io.print.
     */
    fun print(s: Any): IO<Unit> = IO.Suspend { kotlin.io.print(s) }
}

