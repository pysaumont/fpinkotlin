package com.fpinkotlin.optionaldata.listing02

import com.fpinkotlin.optionaldata.exercise06.Option

data class Toon (val firstName: String,
                 val lastName: String,
                 val email: Option<String> = Option()) {

    companion object {
        operator fun invoke(firstName: String,
                            lastName: String,
                            email: String? = null) =
                Toon(firstName, lastName, Option(email))
    }
}

fun <K, V> Map<K, V>.getOption(key: K) = Option(this[key])

fun main(args: Array<String>) {

    val toons: Map<String, Toon>  = mapOf(
            "Mickey" to Toon("Mickey", "Mouse", "mickey@disney.com"),
            "Minnie" to Toon("Minnie", "Mouse"),
            "Donald" to Toon("Donald", "Duck", "donald@disney.com"))

    val mickey = toons.getOption("Mickey").flatMap { it.email }
    val minnie = toons.getOption("Minnie").flatMap { it.email }
    val goofy = toons.getOption("Goofy").flatMap { it.email }

    println(mickey.getOrElse { "No data" })
    println(minnie.getOrElse { "No data" })
    println(goofy.getOrElse { "No data" })
}
