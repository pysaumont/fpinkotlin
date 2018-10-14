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
                                    (random.nextInt(122 - 96) + 96).toChar().let { char ->
                                        Pair("${pair.first}$char", updateMap(pair.second, char))
                                    }
                                }
                    }
                }
            }
}

fun updateMap(map: Map<Char, Int>, char: Char) =  when {
    map.containsKey(char) -> map + Pair(char, map[char]!! + 1)
    else -> map + Pair(char, 1)
}
