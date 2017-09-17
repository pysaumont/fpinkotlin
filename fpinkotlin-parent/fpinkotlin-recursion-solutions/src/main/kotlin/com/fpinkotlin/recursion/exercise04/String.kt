package com.fpinkotlin.recursion.exercise04

fun string(list: List<Char>): String {
    tailrec fun string_(list: List<Char>, acc: String): String =
            if (list.isEmpty())
                acc
            else
                string_(list.tail(), acc + list.head())
    return string_(list, "")
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

fun main(args: Array<String>) {
    println(string(listOf('a', 'b', 'c')))

}
