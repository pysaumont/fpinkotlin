package com.fpinkotlin.advancedtrees.exercise06


import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.common.getOrElse
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class HeapTest: StringSpec() {

    init {

        "plus" {
            val list = List(1, 2, 3, 4, 5, 6, 7)
            val queue: Heap<Int> = list.foldLeft(Heap()) { h -> { h + it } }
            queue.head.map { a -> a == 1 }.getOrElse(false) shouldBe true
            isBalanced(queue) shouldBe true
            isValueOrdered(queue) shouldBe true
        }

        "plus2" {
            val list = List(7, 3, 1, 6, 4, 6, 2)
            val queue: Heap<Int> = list.foldLeft(Heap()) { h -> { h + it } }
            queue.head.map { a -> a == 1 }.getOrElse(false) shouldBe true
            isBalanced(queue) shouldBe true
            isValueOrdered(queue) shouldBe true
        }
    }
}

private fun <A: Comparable<A>> isBalanced(heap: Heap<A>): Boolean =
    rightSpine(heap) <= log2nlz(heap.size + 1)

private fun <A: Comparable<A>> rightSpine(heap: Heap<A>): Int {
    fun <A: Comparable<A>> rightSpine(heap: Result<Heap<A>>): Int =
        heap.map({ t -> if (t.isEmpty) -1 else 1 + rightSpine(t.right) }).getOrElse(-1)
    return 1 + rightSpine(heap.right)
}

private fun <A: Comparable<A>> isValueOrdered(heap: Heap<A>): Boolean {
    fun <A: Comparable<A>> isValueOrderedHelper(heap: Heap<A>): Boolean =
        heap.head.flatMap { t1 ->
            heap.tail().flatMap { tail -> tail.head.map { t2 -> t1 <= t2 } }
        }.getOrElse(true)
    return isValueOrderedHelper(heap) && heap.tail().map { isValueOrderedHelper(it) }.getOrElse(true)
}

fun log2nlz(n: Int): Int = when (n) {
    0    -> 0
    else -> 31 - Integer.numberOfLeadingZeros(n)
}
