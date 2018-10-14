package com.fpinkotlin.lists.exercise01

import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import java.util.*

class ListTest: StringSpec() {

    private val random = Random()

    init {

        "cons0" {
          val i = random.nextInt()
            forAll(IntListGenerator(0, 0), { (first, second) ->
                second.cons(i).toString() == first.let { if (it.isEmpty()) "[$i, NIL]" else it.joinToString(", ", "[$i, ", ", NIL]") }
            }, 1)
        }

        "cons" {
          val i = random.nextInt()
            forAll(IntListGenerator(), { (first, second) ->
                second.cons(i).toString() == first.let { if (it.isEmpty()) "[$i, NIL]" else it.joinToString(", ", "[$i, ", ", NIL]") }
            }, 10)
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
