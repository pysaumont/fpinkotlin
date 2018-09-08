package com.fpinkotlin.recursion.exercise02


object Factorial {
    private lateinit var fact: (Int) -> Int
    init {
        fact = { n -> if (n <= 1) n else n * fact(n - 1) }
    }
    val factorial: (Int)-> Int = fact
}

object Factorial2 {
    val factorial2: (Int)-> Int by lazy { { n: Int -> if (n <= 1) n else n * factorial2(n - 1) } }
}