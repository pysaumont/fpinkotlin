package com.fpinkotlin.advancedtrees.exercise09


import com.fpinkotlin.advancedtrees.common.*
import com.fpinkotlin.generators.IntListGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class HeapTest: StringSpec() {

    init {

        "test Plus Ordered Ascending 7 Comparable" {
            val limit = 7
            val list = range(1, limit + 1)
            val heap = list.foldLeft(Heap()) { h: Heap<Int> -> { i -> h + i } }
            heap.length shouldBe limit
        }

        "test Plus Ordered Descending 7 Comparable" {
            val limit = 7
            val list = unfold(limit) { x -> if (x > 0) Option(Pair(x, x - 1)) else Option() }
            val heap = list.foldLeft(Heap()) { h: Heap<Int> -> { i -> h + i } }
            heap.length shouldBe limit
        }

        "test Plus Ordered Ascending Comparable" {
            val limit = 20_000
            val list = range(1, limit + 1)
            val heap = list.foldLeft(Heap()) { h: Heap<Int> -> { i -> h + i } }
            heap.length shouldBe limit
            isBalanced(heap) shouldBe true
            isValueOrdered(heap) shouldBe true
            val heap2 = list.foldLeft(heap) { t -> { _ -> t.tail().getOrElse(t) } }
            heap2.isEmpty() shouldBe true
        }

        "test Plus Ordered Descending Comparable" {
            val limit = 20_000
            val list = unfold(limit) { x -> if (x > 0) Option(Pair(x, x - 1)) else Option() }
            val heap = list.foldLeft(Heap()) { h: Heap<Int> -> { i -> h + i } }
            heap.length shouldBe limit
            isBalanced(heap) shouldBe true
            isValueOrdered(heap) shouldBe true
            val heap2 = list.foldLeft(heap) { t -> { _ -> t.tail().getOrElse(t) } }
            heap2.isEmpty() shouldBe true
        }

        "plus random Comparable" {
            forAll(IntListGenerator(), { (array, list) ->
                val heap = list.foldLeft(Heap()) { h: Heap<Int> -> { i -> h + i } }
                val heap2 = list.foldLeft(heap) { t -> { _ -> t.tail().getOrElse(t) } }
                heap.length == array.size &&
                    isBalanced(heap) &&
                    isValueOrdered(heap) &&
                    heap2.isEmpty()
            })
        }

        "plus random Comparable 2" {
            forAll(IntListGenerator(), { (array, list) ->
                val heap = list.foldLeft(Heap()) { h: Heap<Int> -> { i -> h + i } }
                val list2 = unfold(heap) { it.pop() }
                val sortedAscending = list2.foldLeft(Pair(true, 0)) { pair ->
                    { n ->
                        Pair(pair.first &&  pair.second <= n, n)
                    }
                }.first
                val sum1 = list.foldLeft(0) { sum -> { n -> sum + n } }
                val sum2 = list2.foldLeft(0) { sum -> { n -> sum + n } }
                sortedAscending && sum1 == sum2
            })
        }
    }
}

private fun <A: Comparable<A>> isBalanced(heap: Heap<A>): Boolean {
    return rightSpine(heap) <= log2nlz(heap.length + 1)
}

private fun <A: Comparable<A>> rightSpine(heap: Heap<A>): Int {
    return 1 + rightSpine(heap.right)
}

private fun <A: Comparable<A>> rightSpine(heap: Result<Heap<A>>): Int {
    return heap.map({ t -> if (t.isEmpty()) -1 else 1 + rightSpine(t.right) }).getOrElse(-1)
}

private fun <A : Comparable<A>> isValueOrdered(heap: Heap<A>): Boolean {
    return isValueOrderedHelper(heap) && heap.tail().map { isValueOrderedHelper(it) }.getOrElse(true)
}

private fun <A : Comparable<A>> isValueOrderedHelper(heap: Heap<A>): Boolean {
    return heap.head.flatMap { t1 -> heap.tail().flatMap { tail -> tail.head.map { t2 -> t1 <= t2 } } }.getOrElse(true)
}

fun log2nlz(n: Int): Int {
    return if (n == 0)
        0
    else
        31 - Integer.numberOfLeadingZeros(n)
}
