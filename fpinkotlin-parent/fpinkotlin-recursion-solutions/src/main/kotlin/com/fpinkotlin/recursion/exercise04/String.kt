package com.fpinkotlin.recursion.exercise04


fun string(list: List<Char>): String {
    tailrec fun string(list: List<Char>, acc: String): String =
            if (list.isEmpty())
                acc
            else
                string(list.tail(), acc + list.head())
    return string(list, "")
}
