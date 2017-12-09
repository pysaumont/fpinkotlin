package com.fpinkotlin.handlingerrors.listing03

import com.fpinkotlin.handlingerrors.exercise05.Result
import java.io.IOException


data class Toon private constructor (val firstName: String,
                val lastName: String,
                val email: Result<String>) {

    companion object {
        operator fun invoke(firstName: String,
                            lastName: String) =
            Toon(firstName, lastName,
                 Result.failure("$firstName $lastName has no mail"))

        operator fun invoke(firstName: String,
                            lastName: String,
                            email: String) =
            Toon(firstName, lastName, Result(email))
    }
}

fun <K, V> Map<K, V>.getResult(key: K) = when {
    this.containsKey(key) -> Result(this[key])
    else -> Result.failure("Key $key not found in map")
}

fun main(args: Array<String>) {

    val toons: Map<String, Toon>  = mapOf(
        "Mickey" to Toon("Mickey", "Mouse", "mickey@disney.com"),
        "Minnie" to Toon("Minnie", "Mouse"),
        "Donald" to Toon("Donald", "Duck", "donald@disney.com"))

    val toon = getName()
        .flatMap(toons::getResult)
        .flatMap(Toon::email)

    println(toon)

}

fun getName(): Result<String> = try {
    validate(readLine())
} catch (e: IOException) {
    Result.failure(e)
}

fun validate(name: String?): Result<String> = when {
    name?.isNotEmpty() ?: false -> Result(name)
    else -> Result.failure("Invalid name $name")
}