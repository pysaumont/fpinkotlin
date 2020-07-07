package com.mydomain.mymultipleproject.common.example04

import io.kotlintest.properties.Gen


fun stringGenerator(maxList: Int,
                    maxString: Int): Gen<List<Pair<String, Map<Char, Int>>>> =
    ListGenerator(ListGenerator(Gen.choose(32, 127), maxString), maxList)
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
