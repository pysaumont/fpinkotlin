package com.fpinkotlin.recursion.exercise04

/**
 * The version printed in the book has a bug because it uses the accumulator
 * to determine whether it is the first recursive call to the function, in
 * order to omit the separator. Otherwise, the result would tart with a
 * separator. This is wrong because if the first element results in an empty
 * string, the accumulator will be empty on the second call. You can
 * experiment this with a list of strings starting with an empty string.
 */
fun <T> makeString_(list: List<T>, delim: String): String {
    tailrec fun makeString_(list: List<T>, acc: String): String = when {
        list.isEmpty() -> acc
        acc.isEmpty() -> makeString_(list.tail(), "${list.head()}")
        else -> makeString_(list.tail(), "$acc$delim${list.head()}")
    }
    return makeString_(list, "")
}

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
