package com.mydomain.mymultipleproject.common.example04

fun main(args: Array<String>) {
    val words = listOf("the", "act", "cat", "is", "bats", "tabs", "tac", "aabc", "abbc", "abca")

    val map = getCharUsed(words)

    println(map)
}

fun getCharUsed(words: List<String>) = words.groupBy(::getCharMap)

fun getCharMap(s: String): Map<Char, Int> {
    val result = mutableMapOf<Char, Int>()
    for (i in 0 until s.length) {
        val ch = s[i]
        if (result.containsKey(ch)) {
            result.replace(ch, result[ch]!! + 1)
        } else {
            result[ch] = 1
        }
    }
    return result
}
