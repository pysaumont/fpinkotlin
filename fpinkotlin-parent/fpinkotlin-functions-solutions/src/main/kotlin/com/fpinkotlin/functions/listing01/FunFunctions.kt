package com.fpinkotlin.functions.listing01


class FunFunctions {

    var percent1 = 5
    private var percent2 = 9
    val percent3 = 13

    fun add(a: Int, b: Int): Int = a + b

    fun mult(a: Int, b: Int?): Int = 5

    fun div(a: Int, b: Int): Int = a / b

    fun div(a: Double, b: Double): Double = a / b

    fun applyTax1(a: Int): Int = a / 100 * (100 + percent1)

    fun applyTax2(a: Int): Int = a / 100 * (100 + percent2)

    fun applyTax3(a: Int): Int = a / 100 * (100 + percent3)

    fun append(i: Int, list: MutableList<Int>): List<Int> {
        list.add(i)
        return list
    }

    fun append2(i: Int, list: List<Int>) = list + i
}

