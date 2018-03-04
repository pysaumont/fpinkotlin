package com.fpinkotlin.effects.listing05

import com.fpinkotlin.effects.listing04.ConsoleReader

fun main(args: Array<String>) {

    val input = ConsoleReader() // <1>

    val rString = input.readString("Enter your name:").map { t -> t.first } // <2>

    val nameMessage = rString.map { "Hello, $it!" } // <3>

    nameMessage.forEach(::println, onFailure = { println(it.message)}) // <4>

    val rInt = input.readInt("Enter your age:").map  { t -> t.first }

    val ageMessage = rInt.map { "You look younger than $it!" }

    ageMessage.forEach(::println, onFailure = { println("Invalid age. Please enter an integer")}) // <5>
}


