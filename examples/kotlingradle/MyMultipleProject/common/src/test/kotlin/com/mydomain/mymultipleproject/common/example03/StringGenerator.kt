package com.mydomain.mymultipleproject.common.example03

import io.kotlintest.properties.Gen
import java.util.*


object StringGenerator : Gen<List<Pair<String, Map<Char, Int>>>> {

    override fun constants(): Iterable<List<Pair<String, Map<Char, Int>>>> =
            listOf(listOf(Pair("", mapOf())))

    override fun random(): Sequence<List<Pair<String, Map<Char, Int>>>> =
            Random().let { random ->
                generateSequence {
                    (0 until random.nextInt(100)).map {
                        (0 until random.nextInt(100))
                                .fold(Pair("", mapOf<Char, Int>())) { pair, _ ->
                                    (random.nextInt(122 - 97) + 97).toChar().let { char ->
                                        Pair("${pair.first}$char", updateMap(pair.second, char))
                                    }
                                }
                    }
                }
            }
}

val stringGenerator: Gen<List<Pair<String, Map<Char, Int>>>> =
    Gen.list(Gen.list(Gen.choose(97, 122)))
        .map { intListList ->
            intListList.asSequence().map { intList ->
                intList.map { n ->
                    n.toChar()
                }
            }.map { charList ->
                Pair(String(charList.toCharArray()), makeMap(charList))
            }.toList()
        }

fun makeMap(charList: List<Char>): Map<Char, Int> = charList.fold(mapOf(), ::updateMap)

fun updateMap(map: Map<Char, Int>, char: Char) =  when {
    map.containsKey(char) -> map + Pair(char, map[char]!! + 1)
    else -> map + Pair(char, 1)
}

fun main(args: Array<String>) {
    StringGenerator.random().take(10).forEach(::println)
    println("====")
    stringGenerator.random().take(10).forEach(::println)
}