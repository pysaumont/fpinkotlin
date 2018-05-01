package com.fpinkotlin.commonproblems.properties.example02

import com.fpinkotlin.common.Result
import java.lang.invoke.MethodHandles
import java.util.*


class PropertyReader(configFileName: String) {

    private val properties: Result<Properties> =
        Result.of {
            MethodHandles.lookup().lookupClass()
                .getResourceAsStream(configFileName)
                .use { inputStream ->
                    Properties().let {
                        it.load(inputStream)
                        it
                    }
                }
        }

    fun readProperty(name: String) =
        properties.flatMap {
            Result.of {
                it.getProperty(name)
            }
        }
}

fun main(args: Array<String>) {
    val propertyReader = PropertyReader("/config.properties") // <7>
    propertyReader.readProperty("host")
            .forEach(onSuccess = { println(it) }, onFailure = { println(it) })
    propertyReader.readProperty("name")
            .forEach(onSuccess = { println(it) }, onFailure = { println(it) })
    propertyReader.readProperty("year")
            .forEach(onSuccess = { println(it) }, onFailure = { println(it) })
}