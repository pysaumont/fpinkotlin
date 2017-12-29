package com.fpinkotlin.workingwithlaziness.exercise26

private val f = { x: Int ->
    println("Mapping " + x)
    x * 3
}

private val p = { x: Int ->
    println("Filtering " + x)
    x % 2 == 0
}

fun main(args: Array<String>) {
//    val list = List(1, 2, 3, 4, 5).map(f).filter(p)
//    println(list)
//    val stream = Stream.from(1).takeAtMost(5).map(f).filter(p)
//    println(stream.toList())
    val stream = Stream.iterate(Lazy{ inc(0) }, ::inc).takeAtMost(5).map(f).filter(p)
}

fun inc(i: Int): Int = (i + 1).let {
    println("generating $it")
    it
}
