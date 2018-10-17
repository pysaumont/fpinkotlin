package com.mydomain.mymultipleproject.common.example06

fun main(args: Array<String>) {
    val words = listOf("the", "act", "cat", "is", "bats", "tabs", "tac", "aabc", "abbc", "abca")

    val map = getCharUsed(words)

    println(map)
}

fun getCharUsed(words: List<String>): Map<Map<Char, Int>, List<String>> = words.groupBy(::getCharMap)

fun getCharMap(s: String): Map<Char, Int> = s.fold(mapOf(), ::updateMap)

fun updateMap(map: Map<Char, Int>, char: Char): Map<Char, Int> =
        when {
            map.containsKey(char) -> map + Pair(char, map[char]!! + 1)
            else -> map + Pair(char, 1)
        }
