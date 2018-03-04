package com.fpinkotlin.effects.exercise03

import com.fpinkotlin.common.Result
import java.io.BufferedReader
import java.io.IOException

abstract class AbstractReader (private val reader: BufferedReader): Input {

    override fun readString(): Result<Pair<String, Input>> = try {
        reader.readLine().let {
            when {
                it.isEmpty() -> Result()
                it == "throw"-> throw IOException("Exception reading input")
                else         -> Result(Pair(it, this))
            }
        }
    } catch (e: Exception) {
        println(e)
        Result.failure(e)
    }

    override fun readInt(): Result<Pair<Int, Input>> = try {
        reader.readLine().let {
            when {
                it.isEmpty() -> Result()
                it == "-1"-> throw IOException("Exception reading input")
                else         -> Result(Pair(it.toInt(), this))
            }
        }
    } catch (e: Exception) {
        println(e)
        Result.failure(e)
    }

    override fun close(): Unit {
        println("Closing!!!!!!!!!!!!!!!!!")
        return reader.close()
    }
}

