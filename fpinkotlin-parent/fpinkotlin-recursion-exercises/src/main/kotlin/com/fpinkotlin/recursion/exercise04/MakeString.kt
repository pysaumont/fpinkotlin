package com.fpinkotlin.recursion.exercise04

fun <T> makeString(list: List<T>, delim: String): String {
    /**
     * In this version, we use a separate counter to know whether it is the first
     * recursive call of not.
     */
    tailrec fun makeString(list: List<T>, acc: String, count: Int): String = when {
        list.isEmpty() -> acc
        count == 0 -> makeString(list.tail(), "${list.head()}", count + 1)
        else -> makeString(list.tail(), "$acc$delim${list.head()}", count + 1)
    }
    return makeString(list, "", 0)
}

fun <T> List<T>.head(): T =
        if (this.isEmpty())
            throw IllegalArgumentException("head called on empty list")
        else
            this[0]

fun <T> List<T>.tail(): List<T> =
        if (this.isEmpty())
            throw IllegalArgumentException("tail called on empty list")
        else
            this.subList(1, this.size)
