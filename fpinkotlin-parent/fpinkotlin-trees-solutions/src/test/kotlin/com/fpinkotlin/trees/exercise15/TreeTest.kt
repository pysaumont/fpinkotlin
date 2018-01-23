package com.fpinkotlin.trees.exercise15


import com.fpinkotlin.common.range
import com.fpinkotlin.trees.exercise15.Tree.Companion.balance
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class TreeTest: StringSpec() {

    /*
     * Adjust according to your environment. The faster the computer,
     * the lower this value should be.
     */
    private val timeFactor = 500

    init {
        "testPlus" {
            val limit = 10_000
            val maxTime = 2 * log2nlz(limit + 1) * timeFactor
            val orderedTestList = range(0, limit)
            val time = System.currentTimeMillis()
            val temp = orderedTestList.foldLeft(Tree<Int>()) { t -> { i -> t + i } }
            println(System.currentTimeMillis() - time)
            val tree = balance(temp)
            val duration = System.currentTimeMillis() - time
            println(duration)
            println(maxTime)
            tree.size shouldBe limit
            (tree.height <= 2 * log2nlz(tree.size + 1)) shouldBe true
            (duration < maxTime) shouldBe true
        }
    }
}
