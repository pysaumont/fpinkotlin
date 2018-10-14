package com.fpinkotlin.effects.exercise02

import com.fpinkotlin.common.Result
import java.io.Closeable


interface Input: Closeable {

    fun readString(): Result<Pair<String, Input>>

    fun readInt(): Result<Pair<Int, Input>>

    fun readString(message: String): Result<Pair<String, Input>> = readString()

    fun readInt(message: String): Result<Pair<Int, Input>> = readInt()
}
