package com.fpinkotlin.commonproblems.properties.example03

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

    fun readProperty(name: String) =
        properties.flatMap {
            Result.of {
                it.getProperty(name)
            }
        }
}

fun main(args: Array<String>) {
    val propertyReader = PropertyReader("/config.properties")
    propertyReader.readProperty("host")
        .forEach(onSuccess = { println(it) }, onFailure = { println(it) })
    propertyReader.readProperty("name")
        .forEach(onSuccess = { println(it) }, onFailure = { println(it) })
    propertyReader.readProperty("year")
        .forEach(onSuccess = { println(it) }, onFailure = { println(it) })
}