package com.fpinkotlin.commonproblems.properties.example04

import com.fpinkotlin.common.Result
import java.io.IOException
import java.lang.invoke.MethodHandles
import java.util.*




class PropertyReader(configFileName: String) {

    private val properties: Result<Properties> =
        try {
            MethodHandles.lookup().lookupClass()
                .getResourceAsStream(configFileName)
                .use { inputStream ->
                    when (inputStream) {
                        null -> Result.failure("File $configFileName not found in classpath")
                        else -> Properties().let {
                            it.load(inputStream)
                            Result(it)
                        }

                    }
                }
        } catch (e: IOException) {
            Result.failure("IOException reading classpath resource $configFileName")
        } catch (e: Exception) {
            Result.failure("Exception: ${e.message}reading classpath resource $configFileName")
        }

    fun readAsString(name: String): Result<String> =
        properties.flatMap {
            Result.of {
                it.getProperty(name)
            }.mapFailure("Property '$name' no found")
        }

    fun readAsInt(name: String): Result<Int> =
        readAsString(name).flatMap {
            try {
                Result(it.toInt())
            } catch (e: NumberFormatException) {
                Result.failure<Int>("Invalid value while parsing property '$name' to Int: $it")
            }
        }
}

data class Person(val id: Int, val firstName: String, val lastName: String)

fun main(args: Array<String>) {
    val propertyReader = PropertyReader("/config.properties")
    val person = propertyReader.readAsInt("id")
        .flatMap { id ->
            propertyReader.readAsString("firstName")
                .flatMap { firstName ->
                    propertyReader.readAsString("lastName")
                        .map { lastName -> Person(id, firstName, lastName) }
                }
        }
    person.forEach(onSuccess = { println(it) }, onFailure = { println(it) })
}
