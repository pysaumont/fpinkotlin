package com.fpinkotlin.advancedlisthandling.exercise06


import com.fpinkotlin.common.Result
import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "sequence" {
            forAll(DoubleListGenerator(), { (first, second) ->
                val errorMessage = "div by 0"
                val f = { x: Double ->  if (x != 0.0) 1 / x else throw IllegalArgumentException(errorMessage) }
                val testList = List(*first)
                val result = sequence(second.map { Result.of{ f(it) } })
                result.toString() == if (product(testList) == 0.0)
                    Result.failure<Double>(errorMessage).toString()
                else Result(testList.map(f)).toString()
            })
        }

        "sequenceLeft" {
            forAll(DoubleListGenerator(), { (first, second) ->
                val errorMessage = "div by 0"
                val f = { x: Double ->  if (x != 0.0) 1 / x else throw IllegalArgumentException(errorMessage) }
                val testList = List(*first)
                val result = sequenceLeft(second.map { Result.of{ f(it) } })
                result.toString() == if (product(testList) == 0.0)
                    Result.failure<Double>(errorMessage).toString()
                else Result(testList.map(f)).toString()
            })
        }
    }
}

class DoubleListGenerator(private val minLength: Int = 0,
                          private val maxLength: Int = 100): Gen<Pair<Array<Double>, List<Double>>> {
    override fun constants(): Iterable<Pair<Array<Double>, List<Double>>> = listOf(Pair(arrayOf(), List()))

    override fun random(): Sequence<Pair<Array<Double>, List<Double>>> =
            list(Gen.double(), minLength, maxLength)
                    .map { Pair(it.toTypedArray(),
                            it.fold(List<Double>()) { list, i ->
                                List.cons(i, list)}) }.random()
}
