package com.fpinkotlin.advancedlisthandling.exercise23


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import java.util.concurrent.Executors

class ListTest: StringSpec() {

    init {

        "parFoldLeft" {
            forAll(IntListGenerator(), { (array, list) ->
                val f = { a: Int -> { b: Int -> a + b } }
                val es = Executors.newFixedThreadPool(4)
                list.parFoldLeft(es, 0, f, f).getOrElse(0) ==  array.sum()
            })
        }
    }
}


class IntListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100): Gen<Pair<Array<Int>, List<Int>>> {
    override fun constants(): Iterable<Pair<Array<Int>, List<Int>>> =
            listOf(Pair(arrayOf(), List()))

    override fun random(): Sequence<Pair<Array<Int>, List<Int>>> =
            list(Gen.int(), minLength, maxLength)
                    .map { Pair(it.toTypedArray(),
                            it.fold(List<Int>()) { list, i ->
                                List.cons(i, list)}) }.random()
}
