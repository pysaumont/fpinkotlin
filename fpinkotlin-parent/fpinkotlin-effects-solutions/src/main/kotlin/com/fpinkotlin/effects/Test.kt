package com.fpinkotlin.effects

import com.fpinkotlin.common.Result


//val inverse: (Int) -> Result<Double> = { x ->
//    when {
//        x != 0 -> Result(1.toDouble() / x)
//        else   -> Result.failure("Division by 0")
//    }
//}

//fun main(args: Array<String>) {
//    val ri: Result<Int> = Result(35)
//    val rd: Result<Double> = ri.flatMap(inverse)
//    val function: (Double) -> Double = { it * 2.35 }
//    val result = rd.map(function)
//    val ef: (Double) -> Unit = ::println
//    val x: Result<Unit> = result.map(ef)
//    println(x)
//}

fun main(args: Array<String>) {
    val ra = Result(4)// <1>
    val rb = Result(0)// <1>
    val inverse: (Int) -> Result<Double> = { x ->
        when {
            x != 0 -> Result(1.toDouble() / x)
            else   -> Result.failure("Division by 0")
        }
    }
    val showResult: (Double) -> Unit = ::println
    val showError: (RuntimeException) -> Unit = { println("Error - ${it.message}")}

    val rt1 = ra.flatMap(inverse)
    val rt2 = rb.flatMap(inverse)

    print("Inverse of 4: ")
    rt1.forEach(showResult, showError) // <2>

    System.out.print("Inverse of 0: ")
    rt2.forEach(showResult, showError) // <3>
}


