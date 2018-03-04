package com.fpinkotlin.effects.listing06

import com.fpinkotlin.common.Result
import com.fpinkotlin.effects.listing02.Input
import com.fpinkotlin.effects.listing03.AbstractReader
import java.io.BufferedReader
import java.io.File


class FileReader private constructor(reader: BufferedReader) : AbstractReader(reader) {

    companion object {

        operator fun invoke(path: String): Result<Input> = try {
            File(path).bufferedReader().use { Result(FileReader(it)) }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
