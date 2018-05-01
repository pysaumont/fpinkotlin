package com.fpinkotlin.commonproblems.properties.example05

import com.fpinkotlin.common.List
import com.fpinkotlin.common.List.Companion.fromSeparated
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

    fun <T> readAsList(name: String, f: (String) -> T): Result<List<T>> =
        readAsString(name).flatMap {
            try {
                Result(fromSeparated(it, ",").map(f))
            } catch (e: Exception) {
                Result.failure<List<T>>("Invalid value while parsing property '$name' to List: $it")
            }
        }

    fun readAsIntList(name: String): Result<List<Int>> = readAsList(name, String::toInt)

    fun readAsDoubleList(name: String): Result<List<Double>> = readAsList(name, String::toDouble)

    fun readAsBooleanList(name: String): Result<List<Boolean>> = readAsList(name, String::toBoolean)

    fun <T> readAsType(f: (String) -> Result<T>, name: String) =
        readAsString(name).flatMap  {
            try {
                f(it)
            } catch (e: Exception) {
                Result.failure<T>("Invalid value while parsing property $name: $it")
            }
        }

    inline fun <reified T: Enum<T>> readAsEnum(name: String, enumClass: Class<T>): Result<T>  {
        val f: (String) -> Result<T> = {
            try {
                val value = enumValueOf<T>(it)
                Result(value)
            } catch (e: Exception) {
                Result.failure("Error parsing property $name: value $it can't be parsed to ${enumClass.name}.")
            }
        }
        return readAsType(f, name)
    }

}

data class Person(val id: Int, val firstName: String, val lastName: String)

enum class Type {SERIAL, PARALLEL}

fun main(args: Array<String>) {
    val propertyReader = PropertyReader("/config.properties")
    val list = propertyReader.readAsIntList("list")
    list.forEach(onSuccess = { println(it) }, onFailure = { println(it) })
    val type = propertyReader.readAsEnum("type", Type::class.java)
    type.forEach(onSuccess = { println(it) }, onFailure = { println(it) })
}
