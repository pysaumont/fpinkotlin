package com.fpinkotlin.effects.exercise08

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object Console {

    private val br = BufferedReader(InputStreamReader(System.`in`))

    fun readln(): IO<String> = IO {
        try {
            br.readLine()
        } catch (e: IOException) {
            throw IllegalStateException(e)
        }
    }

    fun println(o: Any): IO<Unit> = IO {
        kotlin.io.println(o.toString())
    }

    fun print(o: Any): IO<Unit> = IO {
        kotlin.io.print(o.toString())
    }
}
