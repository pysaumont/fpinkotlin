package com.fpinkotlin.effects.listing05

import com.fpinkotlin.effects.listing04.ConsoleReader

fun main(args: Array<String>) {

    val input = ConsoleReader()

    val rString = input.readString("Enter your name:").map { t -> t.first }

    val nameMessage = rString.map { "Hello, $it!" }

    nameMessage.forEach(::println, onFailure = { println(it.message)})

    val rInt = input.readInt("Enter your age:").map  { t -> t.first }

    val ageMessage = rInt.map { "You look younger than $it!" }

    ageMessage.forEach(::println, onFailure = { println("Invalid age. Please enter an integer")})
}


