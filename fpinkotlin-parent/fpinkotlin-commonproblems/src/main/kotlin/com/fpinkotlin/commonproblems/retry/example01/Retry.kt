package com.fpinkotlin.commonproblems.retry.example01

import com.fpinkotlin.common.Result
import java.util.*

fun <A, B> retry(f: (A) -> B, times: Int, delay: Long = 10): (A) -> Result<B> {
    fun retry(a: A, result: Result<B>, e: Result<B>, tms: Int): Result<B> =
            result.orElse {
                when (tms) {
                    0 -> e
                    else -> {
                        Thread.sleep(delay)
                        println("retry ${times - tms}")
                        retry(a, Result.of { f(a) }, result, tms - 1)
                    }
                }
            }
    return { a -> retry(a, Result.of { f(a) }, Result(), times - 1)}
}

fun show(message: String) =
    Random().nextInt(10).let {
        when {
            it < 8 -> throw IllegalStateException("Failure !!!")
            else -> println(message)
        }
    }

fun get(path: String): String =
    Random().nextInt(10).let {
        when {
            it < 8 -> throw IllegalStateException("Error accessing file $path")
            else -> "content of $path"
        }
    }

fun main(args: Array<String>) {

    retry(::show, 10, 20)("Hello, World!").forEach(onFailure = { println(it.message) })
    println()
    retry(::get, 10)("/my/file.txt").forEach({ println(it) }, { println(it.message) })
}