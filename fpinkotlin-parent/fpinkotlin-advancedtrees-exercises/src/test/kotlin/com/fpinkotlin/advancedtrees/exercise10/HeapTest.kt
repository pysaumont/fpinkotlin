package com.fpinkotlin.advancedtrees.exercise10

import com.fpinkotlin.advancedtrees.common.Result
import com.fpinkotlin.advancedtrees.common.getOrElse
import com.fpinkotlin.advancedtrees.common.range
import com.fpinkotlin.advancedtrees.common.unfold
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class HeapTest: StringSpec() {

    init {

        "test Plus Ordered Ascending Comparable" {
            val list = range(1, 2_000)
            val heap = list.foldLeft(Heap()) { h: Heap<Int> -> { i -> h + i } }
            heap.toList().toString() shouldBe(list.toString())
            isBalanced(heap) shouldBe true
            isValueOrdered(heap) shouldBe true
            val heap2 = list.foldLeft(heap) { t -> { _ -> t.tail().getOrElse(t) } }
            heap2.isEmpty shouldBe true
        }

        "test Plus Ordered Descending Comparable" {
            val list = range(1, 2_000)
            val heap = list.reverse().foldLeft(Heap()) { h: Heap<Int> -> { i -> h + i } }
            heap.toList().toString() shouldBe(list.toString())
            isBalanced(heap) shouldBe true
            isValueOrdered(heap) shouldBe true
            val heap2 = list.foldLeft(heap) { t -> { _ -> t.tail().getOrElse(t) } }
            heap2.isEmpty shouldBe true
        }

        "plus random Comparable" {
            forAll(Gen.list(Gen.choose(1, 1_000))) { list ->
                val heap = list.fold(Heap()) { h: Heap<Int>, i -> h + i }
                val heap2 = list.fold(heap) { t, _ -> t.tail().getOrElse(t) }
                heap.size == list.size &&
                    isBalanced(heap) &&
                    isValueOrdered(heap) &&
                    heap2.isEmpty
            }
        }

        "plus random Comparable 2" {
            forAll(Gen.list(Gen.choose(1, 1_000))) { list ->
                val list1 = list.map { Count(it) }
                val heap = list1.fold(Heap(CountComparator)) { h: Heap<Count>, i -> h + i }
                val list2 = unfold(heap) { it.pop() }
                val sortedAscending = list2.foldLeft(Pair(true, Count(0))) { pair ->
                    { count ->
                        Pair(pair.first && CountComparator.compare(pair.second, count) <= 0, count)
                    }
                }.first
                val sum1 = list1.fold(0) { sum, count -> sum + count.value }
                val sum2 = list2.foldLeft(0) { sum -> { count -> sum + count.value } }
                sortedAscending && sum1 == sum2
            }
        }

        "test Plus Ordered Ascending Comparator" {
            val list = range(0, 2_000).map { Count(it) }
            val heap = list.foldLeft(Heap(CountComparator)) { h: Heap<Count> -> { i -> h + i } }
            heap.toList().toString() shouldBe(list.toString())
            isBalanced(heap) shouldBe true
            isValueOrdered(heap, CountComparator) shouldBe true
            val heap2 = list.foldLeft(heap) { t -> { _ -> t.tail().getOrElse(t) } }
            heap2.isEmpty shouldBe true
        }

        "test Plus Ordered Descending Comparator" {
            val list = range(0, 2_000).map { Count(it) }
            val heap = list.reverse().foldLeft(Heap(CountComparator)) { h: Heap<Count> -> { i -> h + i } }
            heap.toList().toString() shouldBe(list.toString())
            isBalanced(heap) shouldBe true
            isValueOrdered(heap, CountComparator) shouldBe true
            val heap2 = list.foldLeft(heap) { t -> { _ -> t.tail().getOrElse(t) } }
            heap2.isEmpty shouldBe true
        }

        "plus random Comparator" {
            forAll(Gen.list(Gen.choose(1, 1_000))) { list ->
                val heap = list.map{ Count(it) }.fold(Heap(CountComparator)) { h: Heap<Count>, i -> h + i }
                val heap2 = list.fold(heap) { t, _ -> t.tail().getOrElse(t) }
                heap.size == list.size &&
                    isBalanced(heap) &&
                    isValueOrdered(heap, CountComparator) &&
                    heap2.isEmpty
            }
        }
    }
}

data class Count(val value: Int): Any() {
    operator fun plus(other: Count): Count = Count(this.value + other.value)
    operator fun minus(other: Count): Count = Count(this.value - other.value)
}

object CountComparator: Comparator<Count> {

    override fun compare(o1: Count, o2: Count): Int = o1.value.compareTo(o2.value)
}

private fun <A> isBalanced(heap: Heap<A>): Boolean {
    fun <A> rightSpine(heap: Heap<A>): Int {
        fun <A> rightSpine(heap: Result<Heap<A>>): Int =
            heap.map { t -> if (t.isEmpty) -1 else 1 + rightSpine(t.right) }.getOrElse(-1)
        return 1 + rightSpine(heap.right)
    }
    return rightSpine(heap) <= log2nlz(heap.size + 1)
}

private fun <A> isValueOrdered(heap: Heap<A>): Boolean {
    fun <A> isValueOrderedHelper(heap: Heap<A>): Boolean =
        heap.head.flatMap { t1 -> heap.tail().flatMap { tail -> tail.head.map { t2 ->
            val comparator = heap.comparator.getOrElse { throw IllegalStateException() }
            comparator.compare(t1, t2) <= 0
        } } }.getOrElse(true)
    return isValueOrderedHelper(heap) && heap.tail().map { isValueOrderedHelper(it) }.getOrElse(true)
}

private fun <A> isValueOrdered(heap: Heap<A>, comparator: Comparator<A>): Boolean {
    fun <A> isValueOrderedHelper(heap: Heap<A>, comparator: Comparator<A>): Boolean =
        heap.head.flatMap { t1 -> heap.tail().flatMap { tail -> tail.head.map { t2 -> comparator.compare(t1, t2) <= 0 } } }.getOrElse(true)
    return isValueOrderedHelper(heap, comparator) && heap.tail().map { isValueOrderedHelper(it, comparator) }.getOrElse(true)
}

fun log2nlz(n: Int): Int = when (n) {
    0 -> 0
    else -> 31 - Integer.numberOfLeadingZeros(n)
}
