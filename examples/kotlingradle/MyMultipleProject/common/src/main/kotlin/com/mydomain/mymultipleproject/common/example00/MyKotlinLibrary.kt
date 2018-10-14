package com.mydomain.mymultipleproject.common.example00

fun maxMultiple(multiple: Int, list: List<Int>): Int {
    var result = 0
    for (i in 1 until list.size) {
        if (list[i] / multiple * multiple == list[i] && list[i] > result) {
            result = list[i]
        }
    }
    return result
}