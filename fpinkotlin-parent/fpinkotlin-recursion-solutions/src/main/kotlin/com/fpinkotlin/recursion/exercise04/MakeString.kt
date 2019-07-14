package com.fpinkotlin.recursion.exercise04

/**
 * the implementation in the book has a bug. It won't work if the first
 * element of the list results in an empty string. This is because the
 * accumulator empty condition is used to detect whether it is the first
 * recursive call.  The problem is that if the first element results in
 * an empty string, the accumulator will still be empty on the second call.
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
