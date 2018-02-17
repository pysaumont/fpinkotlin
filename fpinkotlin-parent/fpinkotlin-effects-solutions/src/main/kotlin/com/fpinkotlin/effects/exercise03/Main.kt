package com.fpinkotlin.effects.exercise03

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.common.Stream

fun readPersonsFromScript(vararg commands: String): List<Person> = Stream.unfold(ScriptReader(*commands), ::person).toList()

fun readPersonsFromConsole(): List<Person> = Stream.unfold(ConsoleReader(), ::person).toList()

fun readPersonsFromFile(path: String): Result<List<Person>> =
        FileReader(path).map { input1 ->
            input1.use { input2 ->
                Stream.unfold(input2, ::person).toList()
            }
        }

fun main(args: Array<String>) {
    val path = "/run/media/pysaumont/KINGSTON2/fpinkotlin/fpinkotlin/fpinkotlin-parent/fpinkotlin-effects-solutions/src/main/kotlin/com/fpinkotlin/effects/exercise03/data.txt"
    readPersonsFromScript("1", "Mickey", "Mouse", "2", "Minnie", "Mouse", "3", "Donald", "Duck").forEach(::println)
    readPersonsFromFile(path).forEach({ list: List<Person> ->
        list.forEach(::println)
    }, onFailure = ::println)
    readPersonsFromConsole().forEach(::println)
}