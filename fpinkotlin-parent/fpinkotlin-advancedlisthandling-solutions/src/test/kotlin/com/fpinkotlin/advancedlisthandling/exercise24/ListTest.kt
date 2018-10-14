package com.fpinkotlin.advancedlisthandling.exercise24

import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import java.util.concurrent.Executors


class ListTest: StringSpec() {

    init {

        "parMap" {
            forAll(IntListGenerator(), { (_, list) ->
                val f = { a: Int -> a * 2 }
                val es = Executors.newFixedThreadPool(4)
                list.parMap(es, f).getOrElse(Result.failure<List<Int>>("Error !")).toString() ==
                        list.map(f).toString()
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
