package com.fpinkotlin.lists.exercise02

import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import java.util.*

class ListTest: StringSpec() {

    private val random = Random()

    init {

        "setHead" {
            val i = random.nextInt()
            forAll(IntListGenerator(1, 100), { (first, second) ->
                second.isEmpty() ||
                second.setHead(i).toString() ==
                        "[$i, ${first.joinToString(", ", "", ", NIL]").substringAfter(", ")}"
            }, 100)
        }

        "setHeadEmpty" {
            val list = List<Int>()
            shouldThrow<IllegalStateException> { list.setHead(1) }
        }
    }
}

class IntListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100): Gen<Pair<Array<Int>, List<Int>>> {

    override fun constants(): Iterable<Pair<Array<Int>, List<Int>>> = listOf(Pair(arrayOf(), List()))

    override fun random(): Sequence<Pair<Array<Int>, List<Int>>> =
            list(Gen.int(), minLength, maxLength)
                    .map { Pair(it.toTypedArray(),
                            it.fold(List<Int>()) { list, i ->
                                list.cons(i) }) }.random()
}
