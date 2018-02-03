package com.fpinkotlin.advancedtrees.exercise02

import com.fpinkotlin.generators.IntListGenerator


fun main(args: Array<String>) {
    println("Generating test data...")
    val list = IntListGenerator(0, 1_000_000, 0, 1_000_000).generate().second
    print("Creating tree... ")
    val time = System.currentTimeMillis()
    val tree = list.foldLeft(Tree<Int>()) { t -> { t + it } }
    val duration = System.currentTimeMillis() - time
    println("inserted ${list.length} elements in $duration ms.")
    val time2 = System.currentTimeMillis()
    print("Emptying tree... ")
    val tree2 = list.foldLeft(tree) { t -> { t.delete (it) } }
    val duration2 = System.currentTimeMillis() - time2
    println("removed ${list.length} elements in $duration2 ms.")
}