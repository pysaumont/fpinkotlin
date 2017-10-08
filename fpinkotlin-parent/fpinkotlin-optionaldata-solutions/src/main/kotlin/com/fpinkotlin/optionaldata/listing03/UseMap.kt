package com.fpinkotlin.optionaldata.listing03

data class Toon (val firstName: String, val lastName: String, val email: String? = null)

fun main(args: Array<String>) {

    val toons: Map<String, Toon>  = mapOf(
            "Mickey" to Toon("Mickey", "Mouse", "mickey@disney.com"),
            "Minnie" to Toon("Minnie", "Mouse"),
            "Donald" to Toon("Donald", "Duck", "donald@disney.com"))

    val mickey = toons["Mickey"]?.email ?: "No data"
    val minnie = toons["Minnie"]?.email ?: "No data"
    val goofy = toons["Goofy"]?.email ?: "No data"

    println(mickey)
    println(minnie)
    println(goofy)
}

