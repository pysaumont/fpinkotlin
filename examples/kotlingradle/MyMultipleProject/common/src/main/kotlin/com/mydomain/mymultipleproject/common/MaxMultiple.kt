package com.mydomain.mymultipleproject.common

//fun maxMultiple(multiple: Int, list: List<Int>): Int {
//    var result = 0
//    for (i in 1 until list.size) {
//        if (list[i] / multiple * multiple == list[i] && list[i] > result) {
//            result = list[i]
//        }
//    }
//    return result
//}

fun isMaxMultiple(multiple: Int) =
        { max: Int, value: Int ->
            when {
                value % multiple == 0 && value > max -> value
                else                                 -> max
            }
        }


fun maxMultiple_(multiple: Int, list: List<Int>): Int {
    var result = 0
    for (i in 1 until list.size) {
        if (list[i] / multiple * multiple == list[i] && list[i] > result) {
            result = list[i]
        }
    }
    return result
}

fun maxMultiple(multiple: Int, list: List<Int>): Int {
    return list.fold(0, isMaxMultiple(multiple))
}
