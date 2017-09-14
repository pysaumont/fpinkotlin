package com.fpinkotlin.lists.exercise10


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import java.util.*

class ListTest: StringSpec() {

    init {

        "foldLeft" {
            forAll(IntListGenerator(), { (_, second) ->
                second.foldLeft(0) { a -> { b -> a + b } } ==  second.foldRight(0) { a -> { b -> a + b } }
            })
        }

        "length" {
            forAll(com.fpinkotlin.lists.exercise08.IntListGenerator(), { (first, second) ->
                second.length() == first.size
            })
        }

        "sum0" {
            forAll(IntListGenerator(0, 0), { (first, second) ->
                sum(second) == first.sum()
            }, 1)
        }

        "sum" {
            forAll(IntListGenerator(), { (first, second) ->
                sum(second) == first.sum()
            })
        }

        "product0" {
            forAll(DoubleListGenerator(0, 0), { (first, second) ->
                Math.abs(product(second) - first.fold(1.0) { a, b -> a * b }) < 0.001
            }, 1)
        }

        "product" {
            forAll(DoubleListGenerator(), { (first, second) ->
                Math.abs(product(second) - first.fold(1.0) { a, b -> a * b }) < 0.001
            })
        }
    }
}

class IntListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100) : Gen<Pair<Array<Int>, List<Int>>> {

    override fun generate(): Pair<Array<Int>, List<Int>> {
        val array: Array<Int> = list(IntGenerator(1_00), minLength, maxLength).generate().toTypedArray()
        return Pair(array, List(*array))
    }
}

class DoubleListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100) : Gen<Pair<Array<Double>, List<Double>>> {

    override fun generate(): Pair<Array<Double>, List<Double>> {
        val array: Array<Double> = list(Gen.double(), minLength, maxLength).generate().toTypedArray()
        return Pair(array, List(*array))
    }
}

class IntGenerator(private val max: Int): Gen<Int> {
    private val RANDOM = Random()
    override fun generate(): Int = RANDOM.nextInt(max)
}

