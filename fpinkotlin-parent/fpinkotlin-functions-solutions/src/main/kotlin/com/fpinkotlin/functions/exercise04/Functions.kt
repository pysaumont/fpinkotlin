package com.fpinkotlin.functions.exercise04


fun square(n: Int) = n * n

fun triple(n: Int) = n * 3

fun <T, U, V> compose(f: (U) -> V, g: (T) -> U): (T) -> V = { f(g(it)) }

val add: (Int) -> (Int) -> Int = { a -> { b -> a + b} }

val compose = { x: (Int) -> Int -> { y: (Int) -> Int -> { z: Int -> x(y(z)) } } }

class Type<out T>

class Type2<in T>

val myList = Type<Int>()

val myList2 = Type2<Any>()

val myParentList: Type<Any> = myList

val myParentList2: Type2<Int> = myList2