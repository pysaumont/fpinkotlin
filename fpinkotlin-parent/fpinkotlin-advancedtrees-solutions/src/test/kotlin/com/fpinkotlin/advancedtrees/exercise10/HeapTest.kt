package com.fpinkotlin.advancedtrees.exercise10

import com.fpinkotlin.advancedtrees.common.*
import com.fpinkotlin.advancedtrees.exercise09.Heap
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

//        "plus random Comparable 2" {
//            forAll(IntListGenerator(), { (array, list) ->
//                val list1 = list.map { Count(it) }
//                val heap = list1.foldLeft(Heap(CountComparator)) { h: Heap<Count> -> { i -> h + i } }
//                val list2 = unfold(heap) { it.pop() }
//                val sortedAscending = list2.foldLeft(Pair(true, Count(0))) { pair ->
//                    { count ->
//                        Pair(pair.first && CountComparator.compare(pair.second, count) <= 0, count)
//                    }
//                }.first
//                val sum1 = list1.foldLeft(0) { sum -> { count -> sum + count.value } }
//                val sum2 = list2.foldLeft(0) { sum -> { count -> sum + count.value } }
//                sortedAscending && sum1 == sum2
//            })
//        }
//
//        "test Plus Ordered Ascending 7 Comparator" {
//            val limit = Count(7)
//            val zero = Count(0)
//            val one = Count(1)
//            val list = unfold(limit) { x: Count -> if (CountComparator.compare(x, zero) < 0) Option(Pair(x, x + one)) else Option() }
//            val heap = list.foldLeft(Heap(CountComparator)) { h: Heap<Count> -> { i -> h + i } }
//            heap.length() shouldBe list.length()
//        }
//
//        "test Plus Ordered Descending 7 Comparator" {
//            val limit = Count(7)
//            val zero = Count(0)
//            val one = Count(1)
//            val list = unfold(limit) { x: Count -> if (CountComparator.compare(x, zero) > 0) Option(Pair(x, x - one)) else Option() }
//            val heap = list.foldLeft(Heap(CountComparator)) { h: Heap<Count> -> { i -> h + i } }
//            heap.length() shouldBe list.length()
//        }

//        "test Plus Ordered Ascending Comparator" {
//            val limit = Count(20_000)
//            val zero = Count(0)
//            val one = Count(1)
//            val list = unfold(limit) { x: Count -> if (CountComparator.compare(x, zero) < 0) Option(Pair(x, x + one)) else Option() }
//            val heap = list.foldLeft(Heap(CountComparator)) { h: Heap<Count> -> { i -> h + i } }
//            heap.length() shouldBe list.length()
//            isBalanced(heap) shouldBe true
//            isValueOrdered(heap, CountComparator) shouldBe true
//            val heap2 = list.foldLeft(heap) { t -> { _ -> t.tail().getOrElse(t) } }
//            heap2.isEmpty() shouldBe true
//        }
//
//        "test Plus Ordered Descending Comparator" {
//            val limit = Count(20_000)
//            val zero = Count(0)
//            val one = Count(1)
//            val list = unfold(limit) { x: Count -> if (CountComparator.compare(x, zero) > 0) Option(Pair(x, x - one)) else Option() }
//            val heap = list.foldLeft(Heap(CountComparator)) { h: Heap<Count> -> { i -> h + i } }
//            heap.length() shouldBe list.length()
//            isBalanced(heap) shouldBe true
//            isValueOrdered(heap, CountComparator) shouldBe true
//            val heap2 = list.foldLeft(heap) { t -> { _ -> t.tail().getOrElse(t) } }
//            heap2.isEmpty() shouldBe true
//        }
//
//        "plus random Comparator" {
//            forAll(IntListGenerator(), { (array, list) ->
//                val heap = list.map{ Count(it) }.foldLeft(Heap(CountComparator)) { h: Heap<Count> -> { i -> h + i } }
//                val heap2 = list.foldLeft(heap) { t -> { _ -> t.tail().getOrElse(t) } }
//                heap.length() == array.size &&
//                        isBalanced(heap) &&
//                        isValueOrdered(heap, CountComparator) &&
//                        heap2.isEmpty()
//            })
//        }
    }
}

data class Count(val value: Int): Any() {
    operator fun plus(other: Count): Count = Count(this.value + other.value)
    operator fun minus(other: Count): Count = Count(this.value - other.value)
}

object CountComparator: Comparator<Count> {

    override fun compare(o1: Count, o2: Count): Int = o1.value.compareTo(o2.value)
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


//private fun <A> isBalanced2(heap: Heap<A>): Boolean {
//    return rightSpine2(heap) <= log2nlz(heap.length() + 1)
//}
//
//private fun <A> rightSpine2(heap: Heap<A>): Int {
//    return 1 + rightSpine2(heap.right())
//}
//
//private fun <A> rightSpine2(heap: Result<Heap<A>>): Int {
//    return heap.map({ t -> if (t.isEmpty()) -1 else 1 + rightSpine2(t.right()) }).getOrElse(-1)
//}

private fun <A : Comparable<A>> isValueOrdered(heap: Heap<A>): Boolean {
    return isValueOrderedHelper(heap) && heap.tail().map { isValueOrderedHelper(it) }.getOrElse(true)
}

private fun <A : Comparable<A>> isValueOrderedHelper(heap: Heap<A>): Boolean {
    return heap.head.flatMap { t1 -> heap.tail().flatMap { tail -> tail.head.map { t2 -> t1 <= t2 } } }.getOrElse(true)
}

private fun <A: Comparable<A>> isValueOrdered(heap: Heap<A>, comparator: Comparator<A>): Boolean {
    return isValueOrderedHelper(heap, comparator) && heap.tail().map { isValueOrderedHelper(it, comparator) }.getOrElse(true)
}

private fun <A: Comparable<A>> isValueOrderedHelper(heap: Heap<A>, comparator: Comparator<A>): Boolean {
    return heap.head.flatMap { t1 -> heap.tail().flatMap { tail -> tail.head.map { t2 -> comparator.compare(t1, t2) <= 0 } } }.getOrElse(true)
}

fun log2nlz(n: Int): Int = when (n) {
    0 -> 0
    else -> 31 - Integer.numberOfLeadingZeros(n)
}
