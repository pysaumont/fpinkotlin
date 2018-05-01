package com.fpinkotlin.commonproblems.properties.example06

import com.fpinkotlin.common.List
import com.fpinkotlin.common.List.Companion.fromSeparated
import com.fpinkotlin.common.Result
import com.fpinkotlin.common.sequence
import com.fpinkotlin.commonproblems.properties.example06.PropertyReader.Companion.stringPropertyReader
import java.io.IOException
import java.io.StringReader
import java.lang.invoke.MethodHandles
import java.util.*

class PropertyReader(private val properties: Result<Properties>, // <1>
                     private val source: String) {

    fun readAsString(name: String): Result<String> =
        properties.flatMap {
            Result.of {
                it.getProperty(name)
            }.mapFailure("Property '$name' not found in $source")
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

    fun readAsPropertyString(propertyName: String): Result<String> =  // <4>
        readAsString(propertyName).map { toPropertyString(it) }

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

    companion object {

        fun toPropertyString(s: String): String = s.replace(";", "\n") // <3>

        private fun readPropertiesFromFile(configFileName: String): Result<Properties> = // <5>
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

        private fun readPropertiesFromString(propString: String): Result<Properties> =// <6>
            try {
                StringReader(propString).use { reader ->
                    val properties = Properties()
                    properties.load(reader)
                    Result(properties)
                }
            } catch (e: Exception) {
                Result.failure("Exception reading property string $propString: ${e.message}")
            }

        fun filePropertyReader(fileName: String): PropertyReader =  // <7>
            PropertyReader(readPropertiesFromFile(fileName), "File: $fileName")

        fun stringPropertyReader(propString: String): PropertyReader = // <8>
            PropertyReader(readPropertiesFromString(propString), "String: $propString")
    }
}

data class Person(val id: Int, val firstName: String, val lastName: String) {

    companion object {
        fun readAsPerson(propertyName: String,
                        propertyReader: PropertyReader): Result<Person> {
            val rString = propertyReader.readAsPropertyString(propertyName)
            val rPropReader = rString.map { stringPropertyReader(it) }
            return rPropReader.flatMap { readPerson(it) }
        }

        fun readAsPersonList(propertyName: String,
                             propertyReader: PropertyReader): Result<List<Person>> =
            propertyReader.readAsList(propertyName, { it }).flatMap { list ->
                     sequence(list.map { s ->
                            readPerson(PropertyReader.stringPropertyReader(PropertyReader.toPropertyString(s)))
                        })
                 }

        private fun readPerson(propReader: PropertyReader): Result<Person> =
            propReader.readAsInt("id")
                .flatMap { id ->
                     propReader.readAsString("firstName")
                         .flatMap { firstName ->
                              propReader.readAsString("lastName")
                                  .map { lastName -> Person(id, firstName, lastName) }
                          }
                 }
    }
}

enum class Type {SERIAL, PARALLEL}

fun main(args: Array<String>) {
    val propertyReader = PropertyReader.filePropertyReader("/config.properties")

    propertyReader.readAsString("host")
        .forEach(onSuccess = { println(it) }, onFailure = { println(it) })

    propertyReader.readAsString("name")
        .forEach(onSuccess = { println(it) }, onFailure = { println(it) })

    propertyReader.readAsString("year")
        .forEach(onSuccess = { println(it) }, onFailure = { println(it) })

    val person1 = propertyReader.readAsInt("id")
        .flatMap({ id ->
             propertyReader.readAsString("firstName")
                 .flatMap({ firstName ->
                      propertyReader.readAsString("lastName")
                          .map({ lastName -> Person(id, firstName, lastName) })
                  })
         })

    person1.forEach(onSuccess = { println(it) }, onFailure = { println(it) })

    val list = propertyReader.readAsIntList("list")
    list.forEach(onSuccess = { println(it) }, onFailure = { println(it) })

    val type = propertyReader.readAsEnum("type", Type::class.java)
    type.forEach(onSuccess = { println(it) }, onFailure = { println(it) })

    val person2 = Person.readAsPerson("person", propertyReader)
    person2.forEach(onSuccess = { println(it) }, onFailure = { println(it) })

    val employees = Person.readAsPersonList("employees", propertyReader)
    employees.forEach(onSuccess = { println(it) }, onFailure = { println(it) })
}

