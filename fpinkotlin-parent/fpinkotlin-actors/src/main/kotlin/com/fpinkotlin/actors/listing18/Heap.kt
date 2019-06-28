package com.asn.pmdatabase.checker.actors01.listing18

import com.fpinkotlin.common.Result
import java.util.*

class Heap<T>(comparator: Comparator<T>): TreeSet<T> (comparator) {

    operator fun plus(element: T): Heap<T> {
        add(element)
        return this
    }

    fun head() = Result.of { first() }

    fun tail() = head().map { head ->
        this.remove(head)
        this
    }
}
