package com.fpinkotlin.effects.exercise03

import com.fpinkotlin.common.Result


interface Input: AutoCloseable {

    fun readString(): Result<Pair<String, Input>>

    fun readInt(): Result<Pair<Int, Input>>

    fun readString(message: String): Result<Pair<String, Input>> = readString()

    fun readInt(message: String): Result<Pair<Int, Input>> = readInt()
}
