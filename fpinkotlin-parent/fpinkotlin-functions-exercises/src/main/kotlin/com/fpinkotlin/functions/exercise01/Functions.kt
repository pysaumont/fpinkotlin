package com.fpinkotlin.functions.exercise01

fun square(n: Int) = n * n

fun triple(n: Int) = n * 3

fun compose(f: (Int) -> Int, g: (Int) -> Int): (Int) -> Int = {f(g(it))}

