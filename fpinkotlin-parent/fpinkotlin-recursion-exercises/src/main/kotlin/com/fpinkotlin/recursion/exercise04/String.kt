package com.fpinkotlin.recursion.exercise04

fun string(list: List<Char>): String  {
    tailrec fun stringTail(list: List<Char>, acc: String): String =
            when {
                list.isEmpty() -> acc
                else -> stringTail(list.tail(), "$acc${list.head()}")
            }
    return stringTail(list, "")
}
