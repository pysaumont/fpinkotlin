package com.fpinkotlin.workingwithlaziness.listing01

fun main(args: Array<String>) {
    val sequence: Sequence<Int> = generateSequence(0, Int::inc)
    println(sequence.first())
    println(sequence.drop(1).first())
    println(sequence.drop(2).first())
    println(sequence.first())
    println(sequence.drop(1).first())
    println(sequence.drop(2).first())
}