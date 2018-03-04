package com.fpinkotlin.effects.exercise03

import com.fpinkotlin.common.Result
import java.io.BufferedReader


class FileReader private constructor(private val reader: BufferedReader) : AbstractReader(reader), AutoCloseable {

    override fun close() = TODO("close")

    companion object {

        operator fun invoke(path: String): Result<Input> = TODO("invoke")
    }
}
