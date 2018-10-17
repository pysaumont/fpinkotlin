package com.mydomain.mymultipleproject.common

import java.io.File
import java.io.IOException

fun method1() = "method 1"

class MyClass {

    companion object {
        fun method2() = "method 2"

        @JvmStatic
        fun method3() = "method 3"
    }
}

object MyObject {

    fun method4() = "method 4"

    @JvmStatic
    fun method5() = "method 5"
}

fun List<String>.concat(): String = this.fold("") { acc, s -> "$acc$s" }

@JvmName("concatIntegers")
fun List<Int>.concat(): String = this.fold("") { acc, i -> "$acc$i" }

@JvmOverloads
fun computePrice(price: Double,
                 tax: Double = 0.20,
                 shipping: Double = 8.75) = price * (1.0 + tax) + shipping

@Throws(IOException::class)
fun readFile(filename: String): String = File(filename).readText()

fun getList() = listOf(1, 2, 3)

fun <A> getFunction() = { a: A -> listOf(a) }

fun getRunnable() = { println("Hello")}

@JvmField
val isEven: (Int) -> Boolean = { it % 2 == 0}

fun concat(vararg strings: String): String = strings.joinToString()


fun main(args: Array<String>) {
    println(concat("abc", "def", "ghi"))
}


fun <T> asList(vararg ts: T): List<T> {
    val result = ArrayList<T>()
    for (t in ts) // ts is an Array
        result.add(t)
    return result
}
