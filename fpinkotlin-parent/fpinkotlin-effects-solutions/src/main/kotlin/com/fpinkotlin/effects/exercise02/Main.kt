package com.fpinkotlin.effects.exercise02

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Stream


fun readPersonsFromConsole(): List<Person> = Stream.unfold(ConsoleReader(), ::person).toList()

fun main(args: Array<String>) {
    readPersonsFromConsole().forEach(::println)
}